package com.example.code_mobile.paginas.code_mobile.cViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.cModel.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.cService.ServiceFiliais
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ViewModelFilial : ViewModel() {
    // Campos do formulário
    var cep = mutableStateOf("")
        private set
    var logradouro = mutableStateOf("")
        private set
    var bairro = mutableStateOf("")
        private set
    var cidade = mutableStateOf("")
        private set
    var estado = mutableStateOf("")
        private set
    var complemento = mutableStateOf("")
        private set
    var num = mutableStateOf("")
        private set
    var id = mutableStateOf(0)
        private set

    // Erros de validação
    var cepError = mutableStateOf<String?>(null)
        private set
    var logradouroError = mutableStateOf<String?>(null)
        private set
    var bairroError = mutableStateOf<String?>(null)
        private set
    var cidadeError = mutableStateOf<String?>(null)
        private set
    var estadoError = mutableStateOf<String?>(null)
        private set
    var complementoError = mutableStateOf<String?>(null)
        private set
    var numError = mutableStateOf<String?>(null)
        private set

    // Feedback de operação
    private val _cadastroSucesso = MutableStateFlow(false)
    val cadastroSucesso: StateFlow<Boolean> = _cadastroSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    // Lista de filiais
    private val _filiais = MutableStateFlow<List<ModelFiliais>>(emptyList())
    val filiais: StateFlow<List<ModelFiliais>> = _filiais

    private val serviceFilial = RetrofithAuth.retrofit.create(ServiceFiliais::class.java)

    // Atualizar campos
    fun atualizarCep(value: String) { cep.value = value; cepError.value = null }
    fun atualizarlogradouro(value: String) { logradouro.value = value; logradouroError.value = null }
    fun atualizarBairro(value: String) { bairro.value = value; bairroError.value = null }
    fun atualizarCidade(value: String) { cidade.value = value; cidadeError.value = null }
    fun atualizarEstado(value: String) { estado.value = value; estadoError.value = null }
    fun atualizarComplemento(value: String) { complemento.value = value; complementoError.value = null }
    fun atualizarNum(value: String) { num.value = value; numError.value = null }

    // Validação
    fun validarCampos(): Boolean {
        var isValid = true

        if (cep.value.isEmpty()) {
            cepError.value = "CEP obrigatório."
            isValid = false
        }
        if (logradouro.value.isEmpty()) {
            logradouroError.value = "Logradouro obrigatório."
            isValid = false
        }
        if (bairro.value.isEmpty()) {
            bairroError.value = "Bairro obrigatório."
            isValid = false
        }
        if (cidade.value.isEmpty()) {
            cidadeError.value = "Cidade obrigatória."
            isValid = false
        }
        if (estado.value.isEmpty()) {
            estadoError.value = "Estado obrigatório."
            isValid = false
        }
        if (num.value.isEmpty()) {
            numError.value = "Número obrigatório."
            isValid = false
        } else if (num.value.any { !it.isDigit() }) {
            numError.value = "Apenas números."
            isValid = false
        }

        return isValid
    }

    // Cadastrar Filial
    fun cadastrarFilial() {
        if (!validarCampos()) return

        viewModelScope.launch {
            _showLoading.value = true
            _mensagemErro.value = null
            try {
                val novaFilial = ModelFiliais(
                    cep = cep.value,
                    logradouro = logradouro.value,
                    bairro = bairro.value,
                    cidade = cidade.value,
                    estado = estado.value,
                    complemento = complemento.value,
                    num = num.value.toInt(),
                    id = null,
                    status = "OPERANTE" // melhoria, criar validação
                )

                val response = withContext(Dispatchers.IO) {
                    serviceFilial.createFilial(novaFilial)
                }

                if (response.isSuccessful) {
                    _cadastroSucesso.value = true
                    limparCampos()
                    carregarFiliais()
                } else {
                    _mensagemErro.value = "Erro ao cadastrar: ${response.code()} - ${response.message()}"
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

    // Limpar campos
    fun limparCampos() {
        cep.value = ""
        logradouro.value = ""
        bairro.value = ""
        cidade.value = ""
        estado.value = ""
        complemento.value = ""
        num.value = ""
    }

    // Carregar lista de filiais
    fun carregarFiliais() {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceFilial.getFiliais()
                }
                if (response.isSuccessful) {
                    _filiais.value = response.body() ?: emptyList()
                } else {
                    _mensagemErro.value = "Erro ao buscar filiais: ${response.message()}"
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

    fun deletarFilial(filial: ModelFiliais) {
        viewModelScope.launch {
            _showLoading.value = true
            _mensagemErro.value = null
            try {
                if(filial.id != null){
                    // Chama o serviço de deleção
                    val response = withContext(Dispatchers.IO) {
                        serviceFilial.deleteFilial(filial.id) // Chama o método delete do seu service
                    }

                    if (response.isSuccessful) {
                        carregarFiliais() // Atualiza a lista de filiais após a exclusão
                    } else {
                        _mensagemErro.value = "Erro ao excluir filial: ${response.code()} - ${response.message()}"
                    }
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
    fun editarFilial(filialID: Int) {
        if (!validarCampos()) return

        viewModelScope.launch {
            _showLoading.value = true
            _mensagemErro.value = null
            try {
                if(filialID != null){
                    val filialAtualizada = ModelFiliais(
                        id = filialID, // Mantém o ID existente
                        cep = cep.value,
                        logradouro = logradouro.value,
                        bairro = bairro.value,
                        cidade = cidade.value,
                        estado = estado.value,
                        complemento = complemento.value,
                        num = num.value.toInt(),
                        status = "OPERANTE" // Mantém o status atual
                    )

                    val response = withContext(Dispatchers.IO) {
                        serviceFilial.updateFilial(filialID ?: return@withContext null, filialAtualizada)
                    }

                    if (response!!.isSuccessful) {
                        carregarFiliais()
                        _cadastroSucesso.value = true
                        limparCampos()
                        // Atualiza a lista com a nova filial editada
                    } else {
                        _mensagemErro.value = "Erro ao editar filial: ${response.code()} - ${response.message()}"
                    }
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


    fun preencherCamposParaEdicao(filial: ModelFiliais) {
        id.value = filial.id ?: 0
        cep.value = filial.cep
        logradouro.value = filial.logradouro
        bairro.value = filial.bairro
        cidade.value = filial.cidade
        estado.value = filial.estado
        num.value = filial.num.toString()
    }


    fun resetarMensagemErro() {
        _mensagemErro.value = null
    }

    fun resetarCadastroSucesso() {
        _cadastroSucesso.value = false
    }
}