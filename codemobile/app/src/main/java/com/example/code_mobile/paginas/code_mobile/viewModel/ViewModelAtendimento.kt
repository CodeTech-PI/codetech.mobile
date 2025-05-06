// VIEWMODELATENDIMENTO.KT
package com.example.code_mobile.paginas.code_mobile.viewModel.atendimento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.paginas.code_mobile.model.ModelAtendimento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelAtendimento : ViewModel() {
    private val _atendimentos = MutableStateFlow<List<ModelAtendimento>>(emptyList())
    val atendimentos: StateFlow<List<ModelAtendimento>> = _atendimentos

    private val _isLoadingAtendimentos = MutableStateFlow(false)
    val isLoadingAtendimentos: StateFlow<Boolean> = _isLoadingAtendimentos

    private val _erroCarregarAtendimentos = MutableStateFlow<String?>(null)
    val erroCarregarAtendimentos: StateFlow<String?> = _erroCarregarAtendimentos

    private val _exclusaoSucesso = MutableStateFlow(false)
    val exclusaoSucesso: StateFlow<Boolean> = _exclusaoSucesso

    private val _mensagemErroExclusao = MutableStateFlow<String?>(null)
    val mensagemErroExclusao: StateFlow<String?> = _mensagemErroExclusao

    init {
        carregarAtendimentos()
    }

    fun carregarAtendimentos() {
        viewModelScope.launch {
            _isLoadingAtendimentos.value = true
            _erroCarregarAtendimentos.value = null
            try {
                val listaMockada = listOf(
                    ModelAtendimento(
                        id = 1,
                        dataAtendimento = "27/03/2025",
                        horarioAtendimento = "10:00",
                        cancelado = false,
                        clienteSelecionado = "Balerina Capuccina",
                        nomeCliente = "Balerina Capuccina",
                        telefoneCliente = "1199999999",
                        emailCliente = "mimimi@email.com",
                        dataNascimentoCliente = "27/03/2002",
                        cpfCliente = "123.456.789-00",
                        produtosUtilizados = listOf("Tinta Preta", "Agulha 0.07"),
                        valorTatuagem = "R$ 150,00"
                    ),
                    ModelAtendimento(
                        id = 2,
                        dataAtendimento = "18/01/2025",
                        horarioAtendimento = "14:30",
                        cancelado = false,
                        clienteSelecionado = "Capuccino",
                        nomeCliente = "Capuccino",
                        telefoneCliente = "1188888888",
                        emailCliente = "mimi@email.com",
                        dataNascimentoCliente = "10/05/1995",
                        cpfCliente = "987.654.321-11",
                        produtosUtilizados = listOf("Luva M"),
                        valorTatuagem = "R$ 200,00"
                    )
                )
                _atendimentos.value = listaMockada
                _isLoadingAtendimentos.value = false
            } catch (e: Exception) {
                _erroCarregarAtendimentos.value = "Erro ao carregar atendimentos: ${e.message}"
                _isLoadingAtendimentos.value = false
            }
        }
    }

    fun excluirAtendimento(
        atendimento: ModelAtendimento,
        onExclusaoSucesso: () -> Unit,
        onExclusaoErro: (String?) -> Unit
    ) {
        viewModelScope.launch {
            _exclusaoSucesso.value = false
            _mensagemErroExclusao.value = null
            try {
                println("Excluindo atendimento com ID: ${atendimento.id}")
                _exclusaoSucesso.value = true
                onExclusaoSucesso()
                carregarAtendimentos()
            } catch (e: Exception) {
                _mensagemErroExclusao.value = "Erro ao excluir atendimento: ${e.message}"
                onExclusaoErro(_mensagemErroExclusao.value)
            }
        }
    }

    fun criarAtendimento(
        dataAtendimento: String,
        horarioAtendimento: String,
        cancelado: Boolean,
        clienteSelecionado: String?,
        nomeCliente: String,
        telefoneCliente: String,
        emailCliente: String,
        dataNascimentoCliente: String,
        cpfCliente: String,
        produtosUtilizados: List<String>,
        valorTatuagem: String,
        onSucesso: (ModelAtendimento) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val novoAtendimento = ModelAtendimento(
                    id = null,
                    dataAtendimento = dataAtendimento,
                    horarioAtendimento = horarioAtendimento,
                    cancelado = cancelado,
                    clienteSelecionado = clienteSelecionado,
                    nomeCliente = nomeCliente,
                    telefoneCliente = telefoneCliente,
                    emailCliente = emailCliente,
                    dataNascimentoCliente = dataNascimentoCliente,
                    cpfCliente = cpfCliente,
                    produtosUtilizados = produtosUtilizados,
                    valorTatuagem = valorTatuagem
                )
                println("Dados do novo atendimento a ser enviado: $novoAtendimento")
                onSucesso(novoAtendimento)
                carregarAtendimentos()
            } catch (e: Exception) {
                onError("Erro ao criar atendimento: ${e.message}")
            }
        }
    }
}