package com.example.code_mobile.paginas.code_mobile.model

data class ModelAgendamento(
    val id: Int,
    val dt: String, // Data no formato ISO (YYYY-MM-DD)
    val horario: ModelHorario,
    val cancelado: Boolean,
    val usuario: ModelUsuario
) {
    // Classe interna para o horário
    data class ModelHorario(
        val hour: Int,
        val minute: Int,
        val second: Int,
        val nano: Int
    )

    // Classe interna para o usuário
    data class ModelUsuario(
        val id: Int,
        val nome: String,
        val cpf: String,
        val telefone: String,
        val email: String,
        val dataNascimento: String // Data no formato ISO (YYYY-MM-DD)
    )
}