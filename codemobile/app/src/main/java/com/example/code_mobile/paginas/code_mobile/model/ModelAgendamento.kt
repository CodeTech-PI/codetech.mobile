package com.example.code_mobile.paginas.code_mobile.model

import com.google.gson.annotations.JsonAdapter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class ModelFaturamento(
    val id: Int,
    val lucro: BigDecimal,
    val ordemServico: ModelOrdemServico
)

data class ModelOrdemServico(
    val id: Int,
    val valorTatuagem: BigDecimal,
    val agendamento: ModelAgendamento
)

data class ModelAgendamento(
    val id: Int,
    val dt: String,          // Data convertida de String para LocalDate
    val horario: String,     // Horário convertido de String para LocalTime
    val cancelado: Boolean,
    val usuario: ModelUsuario
)

data class ModelUsuario(
    val id: Int,
    val nome: String,
    val cpf: String,
    val telefone: String,
    val email: String,
    val dataNascimento: String   // Data convertida de String para LocalDate
)