package com.example.code_mobile.aplicacao_mobile.cViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.aplicacao_mobile.cModel.ModelAgendamento
import com.example.code_mobile.aplicacao_mobile.cModel.ModelFaturamento
import com.example.code_mobile.aplicacao_mobile.cService.ServiceFaturamento
import com.example.code_mobile.aplicacao_mobile.cService.ServiceOrdemServico
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ViewModelFaturamento : ViewModel(){
    private val faturamentoApi: ServiceFaturamento by lazy {
        RetrofithAuth.retrofit.create(ServiceFaturamento::class.java)
    }
    private val _erroCarregarFaturamento = MutableStateFlow<String?>(null)
    val erroCarregarFaturamento: StateFlow<String?> = _erroCarregarFaturamento;

    private val _sucessoCarregarFaturamento = MutableStateFlow<Boolean>(false)
    val sucessoCarregarFaturamento: StateFlow<Boolean> = _sucessoCarregarFaturamento

    private val _isLoadingFaturamento = MutableStateFlow(false)
    val isLoadingFaturamento: StateFlow<Boolean> = _isLoadingFaturamento

    private val _faturamento = MutableStateFlow<ModelFaturamento?>(null)
    val faturamento: StateFlow<ModelFaturamento?> = _faturamento

    fun carregarFaturamento(id: Int) {
        viewModelScope.launch {
            _isLoadingFaturamento.value = true
            _erroCarregarFaturamento.value = null
            _sucessoCarregarFaturamento.value = false;
            try {
                val response = withContext(Dispatchers.IO) {
                    faturamentoApi.listarFaturamento(id)
                }
                if (response.isSuccessful) {
                    _faturamento.value = response.body() ?: null;
                    _sucessoCarregarFaturamento.value = true;
                } else {
                    _erroCarregarFaturamento.value =
                        "Erro ao carregar faturamento: ${response.code()} - ${response.message()}"
                    Log.e(
                        "ViewModelAgendamento",
                        "Erro ao carregar faturamento: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: IOException) {
                _erroCarregarFaturamento.value = "Erro de conexão: ${e.message}"
                Log.e(
                    "ViewModelAgendamento",
                    "Erro de conexão ao carregar faturamento: ${e.message}"
                )
            } catch (e: Exception) {
                _erroCarregarFaturamento.value = "Erro inesperado: ${e.message}"
                Log.e(
                    "ViewModelAgendamento",
                    "Erro inesperado ao carregar faturamento: ${e.message}",
                    e
                )
            } finally {
                _isLoadingFaturamento.value = false
            }
        }
    }
}