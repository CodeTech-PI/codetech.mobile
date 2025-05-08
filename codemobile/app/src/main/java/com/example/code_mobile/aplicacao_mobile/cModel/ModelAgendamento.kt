package com.example.code_mobile.aplicacao_mobile.cModel

import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente

data class ModelAgendamento(
    val id: Int?,
    val dt: String,
    val horario: String,
    val cancelado: Boolean,
    val usuario: ModelCliente?,
)