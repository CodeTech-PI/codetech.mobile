package com.example.code_mobile.aplicacao_mobile.cModel

import java.math.BigDecimal

data class AgendamentoIdWrapper(val id: Int)

data class ModelOrdemServico (
    val id : Int,
    val valorTatuagem : BigDecimal,
    val agendamento: AgendamentoIdWrapper

)