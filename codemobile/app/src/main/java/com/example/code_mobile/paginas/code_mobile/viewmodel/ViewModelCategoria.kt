package com.example.code_mobile.paginas.code_mobile.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelCategoria
import com.example.code_mobile.paginas.code_mobile.service.ServiceCategoria
import kotlinx.coroutines.launch

class CategoriaViewModel(private val service: ServiceCategoria) : ViewModel() {

    private val _categorias = mutableStateOf<List<ModelCategoria>>(emptyList())
    val categorias: State<List<ModelCategoria>> = _categorias

    private val _categoria = mutableStateOf<ModelCategoria?>(null)
    val categoria: State<ModelCategoria?> = _categoria

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        listarCategorias()
    }

    fun listarCategorias() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.getCategorias()
                if (response.isSuccessful) {
                    _categorias.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Erro ao listar categorias: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede ou interno: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarCategoria(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _categoria.value = null
            try {
                val response = service.getCategoriaById(id)
                if (response.isSuccessful) {
                    _categoria.value = response.body()
                } else {
                    _error.value = "Erro ao buscar categoria com ID $id: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede ou interno: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun criarCategoria(categoria: ModelCategoria) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.criarCategoria(categoria)
                if (response.isSuccessful) {
                    listarCategorias() // Recarrega a lista após a criação
                } else {
                    _error.value = "Erro ao criar categoria: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede ou interno: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun atualizarCategoria(id: Int, categoria: ModelCategoria) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.atualizarCategoria(id, categoria)
                if (response.isSuccessful) {
                    listarCategorias() // Recarrega a lista após a atualização
                    buscarCategoria(id) // Atualiza a categoria específica, se necessário
                } else {
                    _error.value = "Erro ao atualizar categoria com ID $id: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede ou interno: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deletarCategoria(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = service.deletarCategoria(id)
                if (response.isSuccessful) {
                    listarCategorias() // Recarrega a lista após a exclusão
                    _categoria.value = null // Limpa a categoria exibida, se for o caso
                } else {
                    _error.value = "Erro ao deletar categoria com ID $id: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Erro de rede ou interno: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}