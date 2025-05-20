package com.example.code_mobile.paginas.code_mobile.viewModel.filial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.cModel.ModelFiliais
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelFilial : ViewModel() {

    private val _filiais = MutableStateFlow<List<ModelFiliais>>(emptyList()) // Use ModelFiliais
    val filiais: StateFlow<List<ModelFiliais>> = _filiais

    private val _isLoadingFiliais = MutableStateFlow(false)
    val isLoadingFiliais: StateFlow<Boolean> = _isLoadingFiliais

    private val _erroCarregarFiliais = MutableStateFlow<String?>(null)
    val erroCarregarFiliais: StateFlow<String?> = _erroCarregarFiliais

    private val _exclusaoSucesso = MutableStateFlow(false)
    val exclusaoSucesso: StateFlow<Boolean> = _exclusaoSucesso

    private val _mensagemErroExclusao = MutableStateFlow<String?>(null)
    val mensagemErroExclusao: StateFlow<String?> = _mensagemErroExclusao

    init {
        carregarFiliais() // Carrega as filiais ao iniciar o ViewModel
    }

    fun carregarFiliais() {
        _isLoadingFiliais.value = true
        _erroCarregarFiliais.value = null
        viewModelScope.launch {
            try {
                // Simule a chamada à sua API ou banco de dados para buscar as filiais
                // Adapte a criação de ModelFiliais com os seus atributos
                val filiaisCarregadas = listOf(
                    ModelFiliais(id = 1,cep = "12345678", logradouro = "Rua Haddock Lobo", bairro = "Cerqueira César", cidade = "São Paulo", estado = "SP", complemento = "Apto 101", num = 123, status = "Operante"),
                    ModelFiliais(id = 2, cep = "87456321", logradouro = "Rua São Marcos", bairro = "Vila Madalena", cidade = "São Paulo", estado = "SP", complemento = "Casa", num = 456, status = "Operante"),
                    ModelFiliais(id = 3, cep = "19141010", logradouro = "Rua Caraíbas", bairro = "Pinheiros", cidade = "São Paulo", estado = "SP", complemento = "Sala 2", num = 789, status = "Inoperante")
                )
                _filiais.value = filiaisCarregadas
            } catch (e: Exception) {
                _erroCarregarFiliais.value = "Erro ao carregar filiais: ${e.localizedMessage}"
            } finally {
                _isLoadingFiliais.value = false
            }
        }
    }

    // Adapte a função deletarFilial para receber e usar a informação de identificação da sua ModelFiliais
    // Assumindo que você tenha algum identificador único (pode ser uma combinação de campos se não houver um 'id' explícito)
    fun deletarFilial(filial: ModelFiliais, onExclusaoSucesso: () -> Unit, onExclusaoErro: (String?) -> Unit) {
        viewModelScope.launch {
            _isLoadingFiliais.value = true
            _mensagemErroExclusao.value = null
            try {
                // Simule a chamada à sua API ou banco de dados para deletar a filial
                // Use os atributos de 'filial' para identificar qual excluir
                println("Tentando excluir a filial: $filial (simulação)")
                _exclusaoSucesso.value = true
                carregarFiliais() // Recarrega a lista após a exclusão
                onExclusaoSucesso()
            } catch (e: Exception) {
                val errorMessage = "Erro ao excluir filial: ${e.localizedMessage}"
                _mensagemErroExclusao.value = errorMessage
                onExclusaoErro(errorMessage)
            } finally {
                _isLoadingFiliais.value = false
            }
        }
    }

    fun resetExclusaoSucesso() {
        _exclusaoSucesso.value = false
    }

    fun setMensagemErroExclusao(mensagem: String?) {
        _mensagemErroExclusao.value = mensagem
    }


}