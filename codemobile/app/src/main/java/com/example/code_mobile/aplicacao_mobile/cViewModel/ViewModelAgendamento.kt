package com.example.code_mobile.aplicacao_mobile.cViewModel

import com.example.code_mobile.aplicacao_mobile.cModel.ModelAgendamento
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_mobile.aplicacao_mobile.cService.ServiceAgendamento
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import com.example.code_mobile.token.network.RetrofithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class ViewModelAgendamento : ViewModel() {

    private val agendamentoService: ServiceAgendamento by lazy {
        RetrofithAuth.retrofit.create(ServiceAgendamento::class.java)
    }

    // Estados para os campos de agendamento
    var dataAgendamento =  mutableStateOf<LocalDate?>(null)
        private set
    var horarioAgendamento =
        mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
        private set
    var clienteSelecionado = mutableStateOf<ModelCliente?>(null)
        private set
    var cancelado = mutableStateOf(false)
        private set
    private val _agendamentoIdCriado = MutableStateFlow<Int?>(null)
    val agendamentoIdCriado: StateFlow<Int?> = _agendamentoIdCriado.asStateFlow()
    var valorTatuagem = mutableStateOf<BigDecimal?>(null)
        private set

    // Estados para erros de validação
    var dataAgendamentoError = mutableStateOf<String?>(null)
        private set
    var horarioAgendamentoError = mutableStateOf<String?>(null)
        private set
    var clienteSelecionadoError = mutableStateOf<String?>(null)
        private set

    // Estados para feedback do agendamento
    private val _agendamentoSucesso = MutableStateFlow(false)
    val agendamentoSucesso: StateFlow<Boolean> = _agendamentoSucesso

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _mensagemErro = MutableStateFlow<String?>(null)
    val mensagemErro: StateFlow<String?> = _mensagemErro

    // Formatter para o horário
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Estados para a tela de agendamentos
    private val _agendamentos = MutableStateFlow<List<ModelAgendamento>>(emptyList())
    val agendamentos: StateFlow<List<ModelAgendamento>> = _agendamentos

    private val _isLoadingAgendamentos = MutableStateFlow(false)
    val isLoadingAgendamentos: StateFlow<Boolean> = _isLoadingAgendamentos

    private val _erroCarregarAgendamentos = MutableStateFlow<String?>(null)
    val erroCarregarAgendamentos: StateFlow<String?> = _erroCarregarAgendamentos

    private val serviceAgendamento = RetrofithAuth.retrofit.create(ServiceAgendamento::class.java)

    // Função para atualizar a data do atendimento
    fun atualizarDataAgendamento(novaData: LocalDate) {
        dataAgendamento.value = novaData
        dataAgendamentoError.value = null
        println("Data Agendamento Atualizada: $novaData")
    }

    // Função para atualizar o horário do atendimento
    fun atualizarHorarioAgendamento(novoHorario: String) {
        horarioAgendamento.value = novoHorario
        horarioAgendamentoError.value = null
        println("Horário Agendamento Atualizado: $novoHorario")
    }
    fun atualizarValor(novoValor: BigDecimal) {
        valorTatuagem.value = novoValor
        println("Horário Agendamento Atualizado: $novoValor")
    }

    // Função para atualizar o cliente selecionado
    fun atualizarClienteSelecionado(novoCliente: ModelCliente?) {
        clienteSelecionado.value = novoCliente
        clienteSelecionadoError.value = null
    }

    // Função para atualizar o status de cancelamento
    fun atualizarCancelado(novoCancelado: Boolean) {
        cancelado.value = novoCancelado
    }

    // Função para validar os campos do agendamento
    fun validarAgendamento(): Boolean {
        var isValid = true
        if (dataAgendamento.value == null) {
            dataAgendamentoError.value = "A data do atendimento é obrigatória."
            isValid = false
        } else {
            dataAgendamentoError.value = null
        }

        if (horarioAgendamento.value.isEmpty()) {
            horarioAgendamentoError.value = "O horário do atendimento é obrigatório."
            isValid = false
        } else {
            try {
                LocalTime.parse(horarioAgendamento.value, timeFormatter)
                horarioAgendamentoError.value = null
            } catch (e: Exception) {
                horarioAgendamentoError.value = "Horário inválido (formato HH:mm)."
                isValid = false
            }
        }

        if (clienteSelecionado.value == null) {
            clienteSelecionadoError.value = "O cliente é obrigatório."
            isValid = false
        } else {
            clienteSelecionadoError.value = null
        }

        return isValid
    }

    fun validarFormulario() {
        val isDataValida = dataAgendamento.value != null
        val isHorarioValido = try {
            LocalTime.parse(horarioAgendamento.value, DateTimeFormatter.ofPattern("HH:mm"))
            true
        } catch (e: Exception) {
            false
        }
        val isClienteSelecionadoValido = clienteSelecionado.value != null

        _isLoadingAgendamentos.value = isDataValida && isHorarioValido && isClienteSelecionadoValido
    }

    // Função para agendar um novo atendimento
    fun cadastrarAgendamento(agendamento: ModelAgendamento) {
        _showLoading.value = true
        viewModelScope.launch {
            try {
                val horarioFormatado = agendamento.horario.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                val dataFormatada = agendamento.dt

                val agendamentoParaEnviar = agendamento.copy(
                    dt = dataFormatada,
                    horario = horarioFormatado,
                    cancelado = agendamento.cancelado,
                    usuario = agendamento.usuario
                )

                val response = agendamentoService.postAgendamento(agendamentoParaEnviar)
                _agendamentoSucesso.value = response.isSuccessful
                _mensagemErro.value = if (response.isSuccessful) null else "Erro ao cadastrar agendamento."
                if (response.isSuccessful) {
                    _agendamentoIdCriado.value = response.body()?.id
                    Log.d("ViewModelAgendamento", "Agendamento cadastrado com sucesso! ID: ${_agendamentoIdCriado.value}")
                }
            } catch (e: HttpException) {
                _mensagemErro.value = "Erro HTTP: ${e.code()} - ${e.message()}"
                Log.e("ViewModelAgendamento", "Erro HTTP ao cadastrar agendamento: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                _mensagemErro.value = "Erro inesperado: ${e.message}"
                Log.e("ViewModelAgendamento", "Erro inesperado ao cadastrar agendamento: ${e.message}", e)
            } finally {
                _showLoading.value = false
            }
        }
    }

    // Função para limpar os campos de agendamento
    fun limparCamposAgendamento() {
        dataAgendamento.value = LocalDate.now()
        horarioAgendamento.value = LocalTime.now().format(timeFormatter)
        clienteSelecionado.value = null
        cancelado.value = false
    }

    // Função para limpar a mensagem de erro
    fun limparMensagemDeErro() {
        _mensagemErro.value = null
    }

    // Função para resetar o estado de sucesso do agendamento
    fun resetAgendamentoSucesso() {
        _agendamentoSucesso.value = false
    }

    // Função para buscar a lista de agendamentos
    fun carregarAgendamentos() {
        viewModelScope.launch {
            _isLoadingAgendamentos.value = true
            _erroCarregarAgendamentos.value = null
            try {
                val response = withContext(Dispatchers.IO) {
                    serviceAgendamento.getAgendamentos()
                }
                if (response.isSuccessful) {
                    _agendamentos.value = response.body() ?: emptyList()
                } else {
                    _erroCarregarAgendamentos.value =
                        "Erro ao carregar agendamentos: ${response.code()} - ${response.message()}"
                    Log.e(
                        "ViewModelAgendamento",
                        "Erro ao carregar agendamentos: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: IOException) {
                _erroCarregarAgendamentos.value = "Erro de conexão: ${e.message}"
                Log.e(
                    "ViewModelAgendamento",
                    "Erro de conexão ao carregar agendamentos: ${e.message}"
                )
            } catch (e: Exception) {
                _erroCarregarAgendamentos.value = "Erro inesperado: ${e.message}"
                Log.e(
                    "ViewModelAgendamento",
                    "Erro inesperado ao carregar agendamentos: ${e.message}",
                    e
                )
            } finally {
                _isLoadingAgendamentos.value = false
            }
        }
    }

    // Função para excluir um agendamento
    fun excluirAgendamento(
        agendamento: ModelAgendamento,
        onExclusaoSucesso: () -> Unit,
        onExclusaoErro: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (agendamento.id != null) {
                    val response = withContext(Dispatchers.IO) {
                        serviceAgendamento.deletarAgendamento(agendamento.id)
                    }
                    if (response.isSuccessful) {
                        Log.i(
                            "ViewModelAgendamento",
                            "Agendamento com ID ${agendamento.id} excluído com sucesso."
                        )
                        carregarAgendamentos() // Recarrega a lista após a exclusão
                        onExclusaoSucesso()
                    } else {
                        val errorBodyExclusao = response.errorBody()?.string()
                        val errorCodeExclusao = response.code()
                        val mensagemErro = when (errorCodeExclusao) {
                            404 -> "Agendamento não encontrado."
                            else -> "Erro ao excluir o agendamento. Tente novamente."
                        }
                        Log.e(
                            "ViewModelAgendamento",
                            "Erro ao excluir agendamento ${agendamento.id} (Código: $errorCodeExclusao): $errorBodyExclusao"
                        )
                        onExclusaoErro(mensagemErro)
                    }
                } else {
                    Log.e(
                        "ViewModelAgendamento",
                        "Erro: ID do agendamento é nulo. Não é possível excluir."
                    )
                    onExclusaoErro("Erro: ID do agendamento é inválido.")
                }
            } catch (e: IOException) {
                Log.e(
                    "ViewModelAgendamento",
                    "Erro de conexão ao excluir agendamento: ${e.message}"
                )
                onExclusaoErro("Erro de conexão: ${e.message}")
            } catch (e: Exception) {
                Log.e(
                    "ViewModelAgendamento",
                    "Erro inesperado ao excluir agendamento: ${e.message}",
                    e
                )
                onExclusaoErro("Erro inesperado: ${e.message}")
            }
        }
    }

    fun atualizarAgendamento(
        agendamento: ModelAgendamento,
        onSucesso: () -> Unit,
        onError: (String?) -> Unit
    ) {
        agendamento.id?.let { agendamentoId ->
            viewModelScope.launch {
                try {
                    val response = withContext(Dispatchers.IO) {
                        serviceAgendamento.putAgendamento(agendamentoId, agendamento)
                    }
                    if (response.isSuccessful) {
                        onSucesso()
                    } else {
                        onError("Erro ao atualizar agendamento: ${response.code()}")
                    }
                } catch (e: IOException) {
                    onError("Erro de conexão: ${e.message}")
                } catch (e: Exception) {
                    onError("Erro inesperado: ${e.message}")
                }
            }
        } ?: run {
            onError("ID do agendamento não pode ser nulo para atualização.")
        }
    }
}