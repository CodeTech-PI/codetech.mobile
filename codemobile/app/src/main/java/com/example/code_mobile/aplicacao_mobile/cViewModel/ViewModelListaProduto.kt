package com.example.code_mobile.aplicacao_mobile.cViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.aplicacao_mobile.cModel.ModelListaProduto
import com.example.code_mobile.aplicacao_mobile.cService.ServiceListaProduto
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ViewModelListaProduto : ViewModel() {

    private val serviceListaProduto: ServiceListaProduto by lazy {
        RetrofithAuth.retrofit.create(ServiceListaProduto::class.java)
    }

    // Estado para um único item de lista de produto (para buscar por ID)
    private val _listaProduto = MutableStateFlow<ModelListaProduto?>(null)
    val listaProduto: StateFlow<ModelListaProduto?> = _listaProduto.asStateFlow()

    private val _isLoadingListaProduto = MutableStateFlow(false)
    val isLoadingListaProduto: StateFlow<Boolean> = _isLoadingListaProduto.asStateFlow()

    private val _erroListaProduto = MutableStateFlow<String?>(null)
    val erroListaProduto: StateFlow<String?> = _erroListaProduto.asStateFlow()

    // Estado para feedback de operações (criar, atualizar, deletar)
    private val _operacaoSucesso = MutableStateFlow(false)
    val operacaoSucesso: StateFlow<Boolean> = _operacaoSucesso.asStateFlow()

    private val _mensagemOperacao = MutableStateFlow<String?>(null)
    val mensagemOperacao: StateFlow<String?> = _mensagemOperacao.asStateFlow()

    // Função para buscar um item de lista de produto por ID
    fun encontrarListaProdutoPorId(id: Int) {
        _isLoadingListaProduto.value = true
        _erroListaProduto.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceListaProduto.encontrarPorId(id)
                }
                if (response.isSuccessful) {
                    _listaProduto.value = response.body()
                } else {
                    _erroListaProduto.value = "Erro ao buscar lista de produto (Código: ${response.code()}): ${response.message()}"
                }
            } catch (e: IOException) {
                _erroListaProduto.value = "Erro de conexão: ${e.message}"
            } catch (e: Exception) {
                _erroListaProduto.value = "Erro inesperado: ${e.message}"
            } finally {
                _isLoadingListaProduto.value = false
            }
        }
    }

    // Função para deletar um item de lista de produto
    fun deletarListaProduto(id: Int, onSucesso: () -> Unit, onError: (String?) -> Unit) {
        _isLoadingListaProduto.value = true
        _erroListaProduto.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceListaProduto.deletarListaProduto(id)
                }
                if (response.isSuccessful) {
                    _operacaoSucesso.value = true
                    _mensagemOperacao.value = "Lista de produto deletada com sucesso."
                    onSucesso()
                } else {
                    _operacaoSucesso.value = false
                    _mensagemOperacao.value = "Erro ao deletar lista de produto (Código: ${response.code()}): ${response.message()}"
                    onError(_mensagemOperacao.value)
                }
            } catch (e: IOException) {
                _operacaoSucesso.value = false
                _mensagemOperacao.value = "Erro de conexão: ${e.message}"
                onError(_mensagemOperacao.value)
            } catch (e: Exception) {
                _operacaoSucesso.value = false
                _mensagemOperacao.value = "Erro inesperado: ${e.message}"
                onError(_mensagemOperacao.value)
            } finally {
                _isLoadingListaProduto.value = false
                resetOperacaoFeedback()
            }
        }
    }

    // Função para criar um novo item de lista de produto
    fun cadastrarListaProduto(produtosSelecionadosMap: Map<Int, Int>, agendamentoId: Int, onSucesso: () -> Unit, onError: (String?) -> Unit) {
        _isLoadingListaProduto.value = true
        _erroListaProduto.value = null
        viewModelScope.launch {
            try {
                val gson = Gson()
                val produtosArray = JsonArray()

                produtosSelecionadosMap.forEach { (produtoId, quantidade) ->
                    val produtoJson = JsonObject().apply {
                        addProperty("quantidade", quantidade)
                        val agendamentoJson = JsonObject().apply {
                            addProperty("id", agendamentoId)
                        }
                        add("agendamento", agendamentoJson)
                        val produtoIdJson = JsonObject().apply {
                            addProperty("id", produtoId)
                        }
                        add("produto", produtoIdJson)
                    }
                    produtosArray.add(produtoJson)
                }

                val requestBody = JsonObject().apply {
                    add("produtos", produtosArray)
                }

                val response = withContext(Dispatchers.IO) {
                    serviceListaProduto.postListaProduto(gson.toJson(requestBody).toRequestBody("application/json".toMediaTypeOrNull()))
                }
                if (response.isSuccessful) {
                    _operacaoSucesso.value = true
                    _mensagemOperacao.value = "Lista de produto criada com sucesso."
                    onSucesso()
                } else {
                    _operacaoSucesso.value = false
                    _mensagemOperacao.value = "Erro ao criar lista de produto (Código: ${response.code()}): ${response.message()}"
                    onError(_mensagemOperacao.value)
                }
            } catch (e: IOException) {
                _operacaoSucesso.value = false
                _mensagemOperacao.value = "Erro de conexão: ${e.message}"
                onError(_mensagemOperacao.value)
            } catch (e: Exception) {
                _operacaoSucesso.value = false
                _mensagemOperacao.value = "Erro inesperado: ${e.message}"
                onError(_mensagemOperacao.value)
            } finally {
                _isLoadingListaProduto.value = false
                resetOperacaoFeedback()
            }
        }
    }

    // Função para atualizar um item de lista de produto
    fun atualizarListaProduto(id: Int, novaListaProduto: ModelListaProduto, onSucesso: () -> Unit, onError: (String?) -> Unit) {
        _isLoadingListaProduto.value = true
        _erroListaProduto.value = null
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceListaProduto.putListaProduto(id, novaListaProduto)
                }
                if (response.isSuccessful) {
                    _operacaoSucesso.value = true
                    _mensagemOperacao.value = "Lista de produto atualizada com sucesso."
                    onSucesso()
                } else {
                    _operacaoSucesso.value = false
                    _mensagemOperacao.value = "Erro ao atualizar lista de produto (Código: ${response.code()}): ${response.message()}"
                    onError(_mensagemOperacao.value)
                }
            } catch (e: IOException) {
                _operacaoSucesso.value = false
                _mensagemOperacao.value = "Erro de conexão: ${e.message}"
                onError(_mensagemOperacao.value)
            } catch (e: Exception) {
                _operacaoSucesso.value = false
                _mensagemOperacao.value = "Erro inesperado: ${e.message}"
                onError(_mensagemOperacao.value)
            } finally {
                _isLoadingListaProduto.value = false
                resetOperacaoFeedback()
            }
        }
    }

    // Função para resetar o feedback de operação
    fun resetOperacaoFeedback() {
        _operacaoSucesso.value = false
        _mensagemOperacao.value = null
    }
}