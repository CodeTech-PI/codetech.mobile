package com.example.code_mobile.paginas.code_mobile.cModel

import com.google.gson.annotations.JsonAdapter
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class ModelFaturamentoDash(
    val id: Int,
    val lucro: BigDecimal,
    val ordemServico: ModelOrdemServicoDash
)

data class ModelOrdemServicoDash(
    val id: Int,
    val valorTatuagem: BigDecimal,
    val agendamento: ModelAgendamentoDash
)

data class ModelAgendamentoDash(
    val id: Int,
    val dt: String,          // Data convertida de String para LocalDate
    val horario: String,     // Horário convertido de String para LocalTime
    val cancelado: Boolean,
    val usuario: ModelUsuarioDash
)

data class ModelUsuarioDash(
    val id: Int,
    val nome: String,
    val cpf: String,
    val telefone: String,
    val email: String,
    val dataNascimento: String   // Data convertida de String para LocalDate
)