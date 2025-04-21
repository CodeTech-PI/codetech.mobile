package com.example.code_mobile.paginas.code_mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.service.ServiceCategoria
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelCategoria : ViewModel() {

    private val service = RetrofithAuth.retrofit.create(ServiceCategoria::class.java)

    private val _categorias = MutableStateFlow<List<ModelCategoria>>(emptyList())
    val categorias: StateFlow<List<ModelCategoria>> = _categorias

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro

    fun carregarCategorias() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = service.getCategorias()
                if (response.isSuccessful) {
                    _categorias.value = response.body() ?: emptyList()
                } else {
                    _erro.value = "Erro ao carregar categorias: ${response.code()}"
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun adicionarCategoria(nome: String) {
        viewModelScope.launch {
            try {
                val response = service.criarCategoria(ModelCategoria(0, nome))
                if (response.isSuccessful) {
                    carregarCategorias()
                } else {
                    _erro.value = "Erro ao adicionar categoria: ${response.code()}"
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão: ${e.message}"
            }
        }
    }

    fun deletarCategoria(id: Int) {
        viewModelScope.launch {
            try {
                val response = service.deletarCategoria(id)
                if (response.isSuccessful) {
                    carregarCategorias()
                } else {
                    _erro.value = "Erro ao deletar categoria"
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão"
            }
        }
    }

    fun atualizarCategoria(id: Int, nome: String) {
        viewModelScope.launch {
            try {
                val response = service.atualizarCategoria(id, ModelCategoria(id, nome))
                if (response.isSuccessful) {
                    carregarCategorias()
                } else {
                    _erro.value = "Erro ao atualizar categoria"
                }
            } catch (e: Exception) {
                _erro.value = "Erro de conexão"
            }
        }
    }

    fun limparErro() {
        _erro.value = null
    }
}
