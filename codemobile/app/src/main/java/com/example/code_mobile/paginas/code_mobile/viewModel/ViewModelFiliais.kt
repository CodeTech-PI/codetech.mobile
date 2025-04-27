package com.example.code_mobile.paginas.code_mobile.viewModel

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

class ViewModelFiliais : ViewModel() {

    var id = mutableStateOf(0)
        private set

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

    var num = mutableStateOf(0)
        private set

    var status = mutableStateOf("Operante") // Novo campo para status
        private set

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

    private val _cadastroSucesso = MutableStateFlow(false)
    val cadastroSucesso: StateFlow<Boolean> = _cadastroSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    private val _listaFiliais = MutableStateFlow<List<ModelFiliais>>(emptyList())
    val listaFiliais: StateFlow<List<ModelFiliais>> = _listaFiliais

    fun atualizarId(novoId: Int) {
        id.value = novoId
    }

    fun atualizarCep(novoCep: String) {
        cep.value = novoCep
        cepError.value = null
    }

    fun atualizarLagradouro(novoLagradouro: String) {
        lagradouro.value = novoLagradouro
        lagradouroError.value = null
    }

    fun atualizarBairro(novoBairro: String) {
        bairro.value = novoBairro
        bairroError.value = null
    }

    fun atualizarCidade(novaCidade: String) {
        cidade.value = novaCidade
        cidadeError.value = null
    }

    fun atualizarEstado(novoEstado: String) {
        estado.value = novoEstado
        estadoError.value = null
    }

    fun atualizarComplemento(novoComplemento: String) {
        complemento.value = novoComplemento
        complementoError.value = null
    }

    fun atualizarNum(novoNum: Int) {
        num.value = novoNum
        numError.value = null
    }

    fun atualizarStatus(novoStatus: String) { // Método para atualizar o status
        if (novoStatus == "Operante" || novoStatus == "Inoperante") {
            status.value = novoStatus
        }
    }

    fun validarCampos(): Boolean {
        var isValid = true
        if (cep.value.isBlank()) {
            cepError.value = "O CEP é obrigatório."
            isValid = false
        }
        if (lagradouro.value.isBlank()) {
            lagradouroError.value = "O logradouro é obrigatório."
            isValid = false
        }
        if (bairro.value.isBlank()) {
            bairroError.value = "O bairro é obrigatório."
            isValid = false
        }
        if (cidade.value.isBlank()) {
            cidadeError.value = "A cidade é obrigatória."
            isValid = false
        }
        if (estado.value.isBlank()) {
            estadoError.value = "O estado é obrigatório."
            isValid = false
        }
        if (complemento.value.isBlank()) {
            complementoError.value = "O complemento é obrigatório."
            isValid = false
        }
        if (num.value <= 0) {
            numError.value = "O número deve ser maior que 0."
            isValid = false
        }
        return isValid
    }

    fun cadastrarFilial(filial: ModelFiliais) {
        if (validarCampos()) {
            viewModelScope.launch {
                _showLoading.value = true
                _mensagemErro.value = null
                try {
                    // No código da ViewModel, você agora vai usar o objeto filial passado
                    val service = RetrofithAuth.retrofit.create(ServiceFiliais::class.java)
                    val response = withContext(Dispatchers.IO) {
                        service.createFilial(filial) // Aqui você usa o objeto que foi passado
                    }

                    if (response.isSuccessful) {
                        _cadastroSucesso.value = true
                        carregarFiliais()
                    } else {
                        _mensagemErro.value = "Erro ao cadastrar filial: ${response.code()}"
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

    fun carregarFiliais() {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val service = RetrofithAuth.retrofit.create(ServiceFiliais::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.getFiliais()
                }
                if (response.isSuccessful) {
                    _listaFiliais.value = response.body() ?: emptyList()
                } else {
                    _mensagemErro.value = "Erro ao buscar filiais: ${response.code()}"
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
    fun deletarFilial(id: Int) {
        viewModelScope.launch {
            _showLoading.value = true
            try {
                val service = RetrofithAuth.retrofit.create(ServiceFiliais::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.deleteFilial(id)
                }
                if (response.isSuccessful) {
                    carregarFiliais()
                } else {
                    _mensagemErro.value = "Erro ao deletar filial: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
            } finally {
                _showLoading.value = false
            }
        }
    }

    fun atualizarFilial(filial: ModelFiliais, onSuccess: () -> Unit = {}) {
        if (!validarCampos()) return

        viewModelScope.launch {
            _showLoading.value = true
            _mensagemErro.value = null

            try {
                val service = RetrofithAuth.retrofit.create(ServiceFiliais::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.updateFilial(filial.id, filial)
                }

                if (response.isSuccessful) {
                    carregarFiliais()
                    onSuccess()
                } else {
                    _mensagemErro.value = "Erro ao atualizar filial: ${response.code()}"
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

    fun getFilialById(id: Int) {
        viewModelScope.launch {
            _showLoading.value = true
            _mensagemErro.value = null

            try {
                val service = RetrofithAuth.retrofit.create(ServiceFiliais::class.java)
                val response = withContext(Dispatchers.IO) {
                    service.getFilialById(id) // Aqui você chama o serviço para buscar uma filial pelo ID
                }

                if (response.isSuccessful) {
                    val filial = response.body() // Aqui você deve verificar o tipo de resposta que espera
                    if (filial != null) {
                        // Atualize os campos do ViewModel com os dados da filial
                        this@ViewModelFiliais.id.value = filial.id  // Usa id.value para acessar o valor e atribuir
                        cep.value = filial.cep
                        lagradouro.value = filial.lagradouro
                        bairro.value = filial.bairro
                        cidade.value = filial.cidade
                        estado.value = filial.estado
                        complemento.value = filial.complemento
                        num.value = filial.num
                        status.value = filial.status
                    }
                } else {
                    _mensagemErro.value = "Erro ao buscar filial: ${response.code()}"
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


    fun limparCampos() {
        id.value = 0
        cep.value = ""
        lagradouro.value = ""
        bairro.value = ""
        cidade.value = ""
        estado.value = ""
        complemento.value = ""
        num.value = 0
        status.value = "Operante" // Resetando o status
    }

    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    fun resetCadastroSucesso() {
        _cadastroSucesso.value = false
    }
}