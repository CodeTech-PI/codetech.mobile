package com.example.code_mobile.paginas.code_mobile.viewModel.cliente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.service.ServiceFiliais
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class FiliaisViewModel(private val serviceFiliais: ServiceFiliais) : ViewModel() {

    // Estados para a lista de filiais
    private val _listaFiliais = MutableStateFlow<List<ModelFiliais>>(emptyList())
    val listaFiliais: StateFlow<List<ModelFiliais>> = _listaFiliais

    // Estados para uma única filial (detalhe, edição)
    private val _filialDetalhe = MutableStateFlow<ModelFiliais?>(null)
    val filialDetalhe: StateFlow<ModelFiliais?> = _filialDetalhe

    // Estados para a criação de uma nova filial
    var novaFilial by mutableStateOf(ModelFiliais("", "", "", "", "", "", 0))
        private set
    var cepError by mutableStateOf<String?>(null)
        private set
    var lagradouroError by mutableStateOf<String?>(null)
        private set
    var bairroError by mutableStateOf<String?>(null)
        private set
    var cidadeError by mutableStateOf<String?>(null)
        private set
    var estadoError by mutableStateOf<String?>(null)
        private set
    var numError by mutableStateOf<String?>(null)
        private set

    // Estados para a edição de uma filial existente
    var filialEmEdicao by mutableStateOf<ModelFiliais?>(null)
        private set

    // Estados de carregamento e erro
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    // Estado de sucesso em alguma operação (criação, atualização, exclusão)
    private val _operacaoSucesso = MutableStateFlow(false)
    val operacaoSucesso: StateFlow<Boolean> = _operacaoSucesso

    init {
        carregarFiliais()
    }

    // Funções para atualizar o estado da nova filial
    fun atualizarCep(novoCep: String) {
        novaFilial = novaFilial.copy(cep = novoCep)
        cepError = null
    }

    fun atualizarLagradouro(novoLagradouro: String) {
        novaFilial = novaFilial.copy(lagradouro = novoLagradouro)
        lagradouroError = null
    }

    fun atualizarBairro(novoBairro: String) {
        novaFilial = novaFilial.copy(bairro = novoBairro)
        bairroError = null
    }

    fun atualizarCidade(novaCidade: String) {
        novaFilial = novaFilial.copy(cidade = novaCidade)
        cidadeError = null
    }

    fun atualizarEstado(novoEstado: String) {
        novaFilial = novaFilial.copy(estado = novoEstado)
        estadoError = null
    }

    fun atualizarComplemento(novoComplemento: String) {
        novaFilial = novaFilial.copy(complemento = novoComplemento)
    }

    fun atualizarNum(novoNum: String) {
        novaFilial = novaFilial.copy(num = novoNum.toIntOrNull() ?: 0)
        numError = null
    }

    // Funções para atualizar o estado da filial em edição
    fun atualizarFilialEmEdicao(filial: ModelFiliais?) {
        filialEmEdicao = filial
    }

    fun atualizarCepEdicao(novoCep: String) {
        filialEmEdicao = filialEmEdicao?.copy(cep = novoCep)
        cepError = null // Reutilizando o mesmo estado de erro por simplicidade
    }

    fun atualizarLagradouroEdicao(novoLagradouro: String) {
        filialEmEdicao = filialEmEdicao?.copy(lagradouro = novoLagradouro)
        lagradouroError = null
    }

    fun atualizarBairroEdicao(novoBairro: String) {
        filialEmEdicao = filialEmEdicao?.copy(bairro = novoBairro)
        bairroError = null
    }

    fun atualizarCidadeEdicao(novaCidade: String) {
        filialEmEdicao = filialEmEdicao?.copy(cidade = novaCidade)
        cidadeError = null
    }

    fun atualizarEstadoEdicao(novoEstado: String) {
        filialEmEdicao = filialEmEdicao?.copy(estado = novoEstado)
        estadoError = null
    }

    fun atualizarComplementoEdicao(novoComplemento: String) {
        filialEmEdicao = filialEmEdicao?.copy(complemento = novoComplemento)
    }

    fun atualizarNumEdicao(novoNum: String) {
        filialEmEdicao = filialEmEdicao?.copy(num = novoNum.toIntOrNull() ?: 0)
        numError = null
    }

    // Função para validar os campos da filial
    fun validarCamposFilial(filial: ModelFiliais): Boolean {
        var isValid = true
        if (filial.cep.isEmpty()) {
            cepError = "O CEP é obrigatório."
            isValid = false
        } else {
            cepError = null
        }

        if (filial.lagradouro.isEmpty()) {
            lagradouroError = "O Logradouro é obrigatório."
            isValid = false
        } else {
            lagradouroError = null
        }

        if (filial.bairro.isEmpty()) {
            bairroError = "O Bairro é obrigatório."
            isValid = false
        } else {
            bairroError = null
        }

        if (filial.cidade.isEmpty()) {
            cidadeError = "A Cidade é obrigatória."
            isValid = false
        } else {
            cidadeError = null
        }

        if (filial.estado.isEmpty()) {
            estadoError = "O Estado é obrigatório."
            isValid = false
        } else if (filial.estado.length != 2) {
            estadoError = "Estado inválido (ex: SP)."
            isValid = false
        } else {
            estadoError = null
        }

        if (filial.num == 0) {
            numError = "O Número é obrigatório."
            isValid = false
        } else {
            numError = null
        }

        return isValid
    }

    // Função para carregar a lista de filiais
    fun carregarFiliais() {
        viewModelScope.launch {
            _isLoading.value = true
            _mensagemErro.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceFiliais.getFiliais()
                }
                if (response.isSuccessful) {
                    _listaFiliais.value = response.body() ?: emptyList()
                } else {
                    _mensagemErro.value = "Erro ao carregar filiais: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _mensagemErro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Função para carregar os detalhes de uma filial
    fun carregarFilial(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _mensagemErro.value = null
            _filialDetalhe.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceFiliais.getFilialById(id)
                }
                if (response.isSuccessful) {
                    _filialDetalhe.value = response.body()
                } else {
                    _mensagemErro.value = "Erro ao carregar filial: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _mensagemErro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Função para criar uma nova filial
    fun criarFilial() {
        if (validarCamposFilial(novaFilial)) {
            viewModelScope.launch {
                _isLoading.value = true
                _mensagemErro.value = null
                try {
                    val response = withContext(Dispatchers.IO) {
                        serviceFiliais.createFilial(novaFilial)
                    }
                    if (response.isSuccessful) {
                        _operacaoSucesso.value = true
                        limparCamposNovaFilial()
                        carregarFiliais() // Recarrega a lista após a criação
                    } else {
                        _mensagemErro.value = "Erro ao criar filial: ${response.code()} - ${response.message()}"
                    }
                } catch (e: IOException) {
                    _mensagemErro.value = "Erro de conexão: ${e.message}"
                } catch (e: Exception) {
                    _mensagemErro.value = "Erro inesperado: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    // Função para atualizar uma filial existente
    fun atualizarFilial(id: Int) {
        filialEmEdicao?.let { filialParaAtualizar ->
            if (validarCamposFilial(filialParaAtualizar)) {
                viewModelScope.launch {
                    _isLoading.value = true
                    _mensagemErro.value = null
                    try {
                        val response = withContext(Dispatchers.IO) {
                            serviceFiliais.updateFilial(id, filialParaAtualizar)
                        }
                        if (response.isSuccessful) {
                            _operacaoSucesso.value = true
                            atualizarFilialEmEdicao(null) // Limpa o estado de edição
                            carregarFiliais() // Recarrega a lista após a atualização
                        } else {
                            _mensagemErro.value = "Erro ao atualizar filial: ${response.code()} - ${response.message()}"
                        }
                    } catch (e: IOException) {
                        _mensagemErro.value = "Erro de conexão: ${e.message}"
                    } catch (e: Exception) {
                        _mensagemErro.value = "Erro inesperado: ${e.message}"
                    } finally {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    // Função para deletar uma filial
    fun deletarFilial(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _mensagemErro.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceFiliais.deleteFilial(id)
                }
                if (response.isSuccessful) {
                    _operacaoSucesso.value = true
                    carregarFiliais() // Recarrega a lista após a exclusão
                } else {
                    _mensagemErro.value = "Erro ao deletar filial: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _mensagemErro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Funções para limpar estados
    fun limparCamposNovaFilial() {
        novaFilial = ModelFiliais("", "", "", "", "", "", 0)
        cepError = null
        lagradouroError = null
        bairroError = null
        cidadeError = null
        estadoError = null
        numError = null
    }

    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    fun resetOperacaoSucesso() {
        _operacaoSucesso.value = false
    }
}