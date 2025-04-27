package com.example.code_mobile.paginas.code_mobile.viewModel.categoria

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.service.ServiceCategoria
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ViewModelCategoria : ViewModel() {

    var nome = mutableStateOf("")
        private set

    var nomeError = mutableStateOf<String?>(null)
        private set

    private val _cadastroSucesso = MutableStateFlow(false)
    val cadastroSucesso: StateFlow<Boolean> = _cadastroSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    private val _listaCategorias = MutableStateFlow<List<ModelCategoria>>(emptyList())
    val listaCategorias: StateFlow<List<ModelCategoria>> = _listaCategorias

    fun atualizarNome(novoNome: String) {
        nome.value = novoNome
        nomeError.value = null
    }

    fun validarCampos(): Boolean {
        var isValid = true
        if (nome.value.isBlank()) {
            nomeError.value = "O nome da categoria é obrigatório."
            isValid = false
        } else {
            nomeError.value = null
        }
        return isValid
    }

    fun cadastrarCategoria() {
        if (validarCampos()) {
            viewModelScope.launch {
                _showLoading.value = true
                _mensagemErro.value = null
                try {
                    val novaCategoria = ModelCategoria(id = 0, nome = nome.value)
                    val service = RetrofithAuth.retrofit.create(ServiceCategoria::class.java)
                    val response = withContext(Dispatchers.IO) {
                        service.criarCategoria(novaCategoria)
                    }

                    if (response.isSuccessful) {
                        _cadastroSucesso.value = true
                        carregarCategorias()
                        limparCampos()
                    } else {
                        _mensagemErro.value = "Erro ao cadastrar categoria: ${response.code()}"
                    }
                } catch (e: IOException) {
                    _mensagemErro.value = "Erro de conexão: ${e.message}"
                } catch (e: Exception) {
                    _mensagemErro.value = "Erro inesperado: ${e.message}"
                } finally {
                    _showLoading.value = false
                }
            }
        }
    }

    fun carregarCategorias() {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val service = RetrofithAuth.retrofit.create(ServiceCategoria::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.getCategorias()
                }
                if (response.isSuccessful) {
                    _listaCategorias.value = response.body() ?: emptyList()
                } else {
                    _mensagemErro.value = "Erro ao buscar categorias: ${response.code()}"
                }
            } catch (e: IOException) {
                _mensagemErro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _showLoading.value = false
            }
        }
    }

    fun deletarCategoria(id: Int) {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val service = RetrofithAuth.retrofit.create(ServiceCategoria::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.deletarCategoria(id)
                }
                if (response.isSuccessful) {
                    carregarCategorias()
                } else {
                    _mensagemErro.value = "Erro ao deletar categoria: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _showLoading.value = false
            }
        }
    }


    fun atualizarCategoria(categoria: ModelCategoria, onSuccess: () -> Unit = {}) {
        if (categoria.nome.isBlank()) {
            nomeError.value = "O nome da categoria é obrigatório."
            return
        }

        viewModelScope.launch {
            _showLoading.value = true
            _mensagemErro.value = null

            try {
                val service = RetrofithAuth.retrofit.create(ServiceCategoria::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.atualizarCategoria(categoria.id, categoria)
                }

                if (response.isSuccessful) {
                    carregarCategorias()
                    onSuccess() // Callback para fechar o dialog ou mostrar uma mensagem
                } else {
                    _mensagemErro.value = "Erro ao atualizar categoria: ${response.code()}"
                }
            } catch (e: IOException) {
                _mensagemErro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _showLoading.value = false
            }
        }
    }

    fun limparCampos() {
        nome.value = ""
    }

    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    fun resetCadastroSucesso() {
        _cadastroSucesso.value = false
    }
}
