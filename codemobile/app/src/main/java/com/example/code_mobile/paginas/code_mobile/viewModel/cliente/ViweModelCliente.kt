package com.example.code_mobile.paginas.code_mobile.viewModel.cliente

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import com.example.code_mobile.paginas.code_mobile.service.ServiceCliente
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ViweModelCliente : ViewModel() {
    var nome = mutableStateOf("")
        private set
    var cpf = mutableStateOf("")
        private set
    var dataNasc = mutableStateOf("")
        private set
    var telefone = mutableStateOf("")
        private set
    var email = mutableStateOf("")
        private set
    var nomeError = mutableStateOf<String?>(null)
        private set
    var cpfError = mutableStateOf<String?>(null)
        private set
    var dataNascError = mutableStateOf<String?>(null)
        private set
    var telefoneError = mutableStateOf<String?>(null)
        private set
    var emailError = mutableStateOf<String?>(null)
        private set

    private val _cadastroSucesso = MutableStateFlow(false)
    val cadastroSucesso: StateFlow<Boolean> = _cadastroSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private set

    fun atualizarNome(novoNome: String) {
        nome.value = novoNome
        nomeError.value = null
    }

    fun atualizarCpf(novoCpf: String) {
        Log.d("CPF_INPUT", "Novo CPF: $novoCpf")
        cpf.value = novoCpf
        cpfError.value = null
    }

    fun atualizarDataNasc(novaDataNasc: String) {
        dataNasc.value = novaDataNasc
        dataNascError.value = null
    }

    fun atualizarTelefone(novoTelefone: String) {
        telefone.value = novoTelefone
        telefoneError.value = null
    }

    fun atualizarEmail(novoEmail: String) {
        email.value = novoEmail
        emailError.value = null
    }

    fun validarCampos(): Boolean {
        var isValid = true
        if (nome.value.isEmpty()) {
            nomeError.value = "O nome é obrigatório."
            isValid = false
        } else {
            nomeError.value = null
        }

        if (cpf.value.isEmpty()) {
            cpfError.value = "O CPF é obrigatório."
            isValid = false
        } else if (cpf.value.filter { it.isDigit() }.length != 11) {
            cpfError.value = "CPF inválido."
            Log.i("ClienteCadastroViewModel", "CPF inválido: ${cpf.value}")
            isValid = false
        } else {
            cpfError.value = null
        }

        if (dataNasc.value.isEmpty()) {
            dataNascError.value = "A data de nascimento é obrigatória."
            isValid = false
        } else {
            dataNascError.value = null
        }

        if (telefone.value.isEmpty()) {
            telefoneError.value = "O telefone é obrigatório."
            isValid = false
        } else if (telefone.value.length < 8) {
            telefoneError.value = "Telefone inválido."
            isValid = false
        } else {
            telefoneError.value = null
        }

        if (email.value.isEmpty()) {
            emailError.value = "O e-mail é obrigatório."
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            emailError.value = "E-mail inválido."
            isValid = false
        } else {
            emailError.value = null
        }

        return isValid
    }

    fun cadastrarCliente() {
        if (validarCampos()) {
            viewModelScope.launch {
                _showLoading.value = true
                _mensagemErro.value = null
                try {
                    val novoCliente = ModelCliente(
                        id = null,
                        nome = nome.value,
                        cpf = cpf.value,
                        dataNascimento = LocalDate.parse(dataNasc.value, dateFormatter).toString(),
                        telefone = telefone.value.filter { it.isDigit() },
                        email = email.value
                    )

                    Log.i("ClienteCadastroViewModel", "Dados do novoCliente antes do envio:")
                    println("Nome: ${novoCliente.nome}")
                    println("CPF: ${novoCliente.cpf}")
                    println("Data de Nascimento: ${novoCliente.dataNascimento}")
                    println("Telefone: ${novoCliente.telefone}")
                    println("Email: ${novoCliente.email}")
                    println("------------------------------------")

                    val response = withContext(Dispatchers.IO) {
                        RetrofithAuth.retrofit.create(ServiceCliente::class.java).postUsuario(novoCliente)
                    }

                    if (response.isSuccessful) {
                        _cadastroSucesso.value = true
                        limparCampos()
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
    }

    fun limparCampos() {
        nome.value = ""
        cpf.value = ""
        dataNasc.value = ""
        telefone.value = ""
        email.value = ""
    }

    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    fun resetCadastroSucesso() {
        _cadastroSucesso.value = false
    }
}