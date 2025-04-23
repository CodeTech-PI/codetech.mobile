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

class ViewModelCliente : ViewModel() {
    // Estados para os campos de cadastro
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

    // Estados para erros de validação
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

    // Estados para feedback do cadastro
    private val _cadastroSucesso = MutableStateFlow(false)
    val cadastroSucesso: StateFlow<Boolean> = _cadastroSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    // Formatter para a data de nascimento
    var dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        private set

    // Estados para a tela de clientes
    private val _clientes = MutableStateFlow<List<ModelCliente>>(emptyList())
    val clientes: StateFlow<List<ModelCliente>> = _clientes

    private val _isLoadingClientes = MutableStateFlow(false)
    val isLoadingClientes: StateFlow<Boolean> = _isLoadingClientes

    private val _erroCarregarClientes = MutableStateFlow<String?>(null)
    val erroCarregarClientes: StateFlow<String?> = _erroCarregarClientes

    private val serviceCliente = RetrofithAuth.retrofit.create(ServiceCliente::class.java)

    // Função para atualizar o nome
    fun atualizarNome(novoNome: String) {
        nome.value = novoNome
        nomeError.value = null
    }

    // Função para atualizar o CPF
    fun atualizarCpf(novoCpf: String) {
        Log.d("CPF_INPUT", "Novo CPF: $novoCpf")
        cpf.value = novoCpf
        cpfError.value = null
    }

    // Função para atualizar a data de nascimento
    fun atualizarDataNasc(novaDataNasc: String) {
        dataNasc.value = novaDataNasc
        dataNascError.value = null
    }

    // Função para atualizar o telefone
    fun atualizarTelefone(novoTelefone: String) {
        telefone.value = novoTelefone
        telefoneError.value = null
    }

    // Função para atualizar o e-mail
    fun atualizarEmail(novoEmail: String) {
        email.value = novoEmail
        emailError.value = null
    }

    // Função para validar todos os campos do formulário
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

    // Função para cadastrar um novo cliente
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
                        serviceCliente.postUsuario(novoCliente)
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

    // Função para limpar os campos do formulário
    fun limparCampos() {
        nome.value = ""
        cpf.value = ""
        dataNasc.value = ""
        telefone.value = ""
        email.value = ""
    }

    // Função para limpar a mensagem de erro
    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    // Função para resetar o estado de sucesso do cadastro
    fun resetCadastroSucesso() {
        _cadastroSucesso.value = false
    }

    // Função para buscar a lista de clientes
    fun carregarClientes() {
        viewModelScope.launch {
            _isLoadingClientes.value = true
            _erroCarregarClientes.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceCliente.getUsuarios()
                }
                if (response.isSuccessful) {
                    _clientes.value = response.body() ?: emptyList()
                } else {
                    _erroCarregarClientes.value =
                        "Erro ao carregar clientes: ${response.code()} - ${response.message()}"
                    Log.e("ViweModelCliente", "Erro ao carregar clientes: ${response.code()} - ${response.message()}")
                }
            } catch (e: IOException) {
                _erroCarregarClientes.value = "Erro de conexão: ${e.message}"
                Log.e("ViweModelCliente", "Erro de conexão ao carregar clientes: ${e.message}")
            } catch (e: Exception) {
                _erroCarregarClientes.value = "Erro inesperado: ${e.message}"
                Log.e("ViweModelCliente", "Erro inesperado ao carregar clientes: ${e.message}", e)
            } finally {
                _isLoadingClientes.value = false
            }
        }
    }

    // Função para excluir um cliente
    fun excluirCliente(cliente: ModelCliente, onExclusaoSucesso: () -> Unit, onExclusaoErro: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (cliente.id != null) {
                    val response = withContext(Dispatchers.IO) {
                        serviceCliente.deletarUsuario(cliente.id)
                    }
                    if (response.isSuccessful) {
                        Log.i("ViweModelCliente", "Cliente com ID ${cliente.id} excluído com sucesso.")
                        carregarClientes() // Recarrega a lista após a exclusão
                        onExclusaoSucesso()
                    } else {
                        val errorBodyExclusao = response.errorBody()?.string()
                        val errorCodeExclusao = response.code()
                        val mensagemErro = when (errorCodeExclusao) {
                            404 -> "Cliente não encontrado."
                            else -> "Erro ao excluir o cliente. Tente novamente."
                        }
                        Log.e("ViweModelCliente", "Erro ao excluir cliente ${cliente.id} (Código: $errorCodeExclusao): $errorBodyExclusao")
                        onExclusaoErro(mensagemErro)
                    }
                } else {
                    Log.e("ViweModelCliente", "Erro: ID do cliente é nulo. Não é possível excluir.")
                    onExclusaoErro("Erro: ID do cliente é inválido.")
                }
            } catch (e: IOException) {
                Log.e("ViweModelCliente", "Erro de conexão ao excluir cliente: ${e.message}")
                onExclusaoErro("Erro de conexão: ${e.message}")
            } catch (e: Exception) {
                Log.e("ViweModelCliente", "Erro inesperado ao excluir cliente: ${e.message}", e)
                onExclusaoErro("Erro inesperado: ${e.message}")
            }
        }
    }
}