package com.example.code_mobile.aplicacao_mobile.cViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.aplicacao_mobile.cModel.ModelOrdemServico
import com.example.code_mobile.aplicacao_mobile.cService.ServiceOrdemServico
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.math.BigDecimal

class ViewModelOrdemServico  : ViewModel() {

    private val serviceOrdemServico: ServiceOrdemServico by lazy {
        RetrofithAuth.retrofit.create(serviceOrdemServico::class.java)
    }

    private val _ordensServico = MutableStateFlow<List<ModelOrdemServico>>(emptyList())
    val ordensServico: StateFlow<List<ModelOrdemServico>> = _ordensServico

    private val _ordemServico = MutableStateFlow<ModelOrdemServico?>(null)
    val ordemServico: StateFlow<ModelOrdemServico?> = _ordemServico

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro

    fun resetFeedback() {
        _mensagem.update { null }
        _erro.update { null }
    }

    fun listarOrdensServico() {
        _isLoading.value = true
        _erro.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceOrdemServico.listarOrdensServico()
                }
                if (response.isSuccessful) {
                    _ordensServico.value = response.body() ?: emptyList()
                } else {
                    _erro.value = "Erro ao listar ordens de serviço: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _erro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _erro.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarOrdemServicoPorId(id: Int) {
        _isLoading.value = true
        _erro.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceOrdemServico.buscarOrdemServicoPorId(id)
                }
                if (response.isSuccessful) {
                    _ordemServico.value = response.body()
                } else {
                    _erro.value = "Erro ao buscar ordem de serviço: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _erro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _erro.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cadastrarOrdemServico(valorTatuagem: BigDecimal, agendamentoId: Int, onSucesso: (ModelOrdemServico?) -> Unit) {
        _isLoading.value = true
        _erro.value = null
        viewModelScope.launch {
            try {
                val novaOrdemServico = ModelOrdemServico(id = 0, valorTatuagem = valorTatuagem, agendamento = agendamentoId) // O ID provavelmente será gerado pelo backend
                val response = withContext(Dispatchers.IO) {
                    serviceOrdemServico.criarOrdemServico(novaOrdemServico)
                }
                if (response.isSuccessful) {
                    _mensagem.value = "Ordem de serviço criada com sucesso."
                    onSucesso(response.body())
                } else {
                    _erro.value = "Erro ao criar ordem de serviço: ${response.code()} - ${response.message()}"
                    onSucesso(null)
                }
            } catch (e: IOException) {
                _erro.value = "Erro de conexão: ${e.message}"
                onSucesso(null)
            } catch (e: Exception) {
                _erro.value = "Erro inesperado: ${e.message}"
                onSucesso(null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun atualizarOrdemServico(id: Int, valorTatuagem: BigDecimal, agendamentoId: Int, onSucesso: (ModelOrdemServico?) -> Unit) {
        _isLoading.value = true
        _erro.value = null
        viewModelScope.launch {
            try {
                val ordemServicoAtualizada = ModelOrdemServico(id = id, valorTatuagem = valorTatuagem, agendamento = agendamentoId)
                val response = withContext(Dispatchers.IO) {
                    serviceOrdemServico.atualizarOrdemServico(id, ordemServicoAtualizada)
                }
                if (response.isSuccessful) {
                    _mensagem.value = "Ordem de serviço atualizada com sucesso."
                    onSucesso(response.body())
                } else {
                    _erro.value = "Erro ao atualizar ordem de serviço: ${response.code()} - ${response.message()}"
                    onSucesso(null)
                }
            } catch (e: IOException) {
                _erro.value = "Erro de conexão: ${e.message}"
                onSucesso(null)
            } catch (e: Exception) {
                _erro.value = "Erro inesperado: ${e.message}"
                onSucesso(null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarOrdensServicoPorPreco(valorProcurado: Double) {
        _isLoading.value = true
        _erro.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceOrdemServico.buscarOrdemServicoPorPreco(valorProcurado)
                }
                if (response.isSuccessful) {
                    _ordensServico.value = response.body() ?: emptyList()
                } else {
                    _erro.value = "Erro ao buscar ordens de serviço por preço: ${response.code()} - ${response.message()}"
                }
            } catch (e: IOException) {
                _erro.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _erro.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}