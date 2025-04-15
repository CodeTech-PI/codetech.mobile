package com.example.code_mobile.paginas.code_mobile.model

data class ModelFaturamento(
    val id: Int,
    val lucro: Double, // Ou Int se for valor inteiro
    val ordemServico: ModelOrdemServico
) {
    data class ModelOrdemServico(
        val id: Int,
        val valorTatuagem: Double, // Ou Int se for valor inteiro
        val agendamento: ModelAgendamento
    ) {
        data class ModelAgendamento(
            val id: Int,
            val dt: String, // Data no formato ISO
            val horario: ModelHorario,
            val cancelado: Boolean,
            val usuario: ModelUsuario
        ) {
            data class ModelHorario(
                val hour: Int,
                val minute: Int,
                val second: Int,
                val nano: Int
            )

            data class ModelUsuario(
                val id: Int,
                val nome: String,
                val cpf: String,
                val telefone: String,
                val email: String,
                val dataNascimento: String // Data no formato ISO
            )
        }
    }
}