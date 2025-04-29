package com.example.code_mobile.paginas.code_mobile.viewModel.filial

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais
import com.example.code_mobile.paginas.code_mobile.service.ServiceFiliais
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
    var lagradouro = mutableStateOf("")
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

    // Erros de validação
    var cepError = mutableStateOf<String?>(null)
        private set
    var lagradouroError = mutableStateOf<String?>(null)
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
    fun atualizarLagradouro(value: String) { lagradouro.value = value; lagradouroError.value = null }
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
        if (lagradouro.value.isEmpty()) {
            lagradouroError.value = "Logradouro obrigatório."
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
                    lagradouro = lagradouro.value,
                    bairro = bairro.value,
                    cidade = cidade.value,
                    estado = estado.value,
                    complemento = complemento.value,
                    num = num.value.toInt()
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
        lagradouro.value = ""
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

    fun resetarMensagemErro() {
        _mensagemErro.value = null
    }

    fun resetarCadastroSucesso() {
        _cadastroSucesso.value = false
    }
}
