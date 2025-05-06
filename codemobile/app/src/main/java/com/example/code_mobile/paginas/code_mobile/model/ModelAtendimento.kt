package com.example.code_mobile.paginas.code_mobile.model

data class ModelAtendimento(
    val id: Int?,
    val dataAtendimento: String, // Usado para "Data" na Etapa 1 e "Data do Atendimento" na Etapa 3 e 4 taokei lula13
    val horarioAtendimento: String, // Usado para "Horário" na Etapa 1 e "Horário do Atendimento" na Etapa 3 e 4 bolsonaro presidente
    val cancelado: Boolean,
    val clienteSelecionado: String?,
    val nomeCliente: String,
    val telefoneCliente: String,
    val emailCliente: String,
    val dataNascimentoCliente: String,
    val cpfCliente: String,
    val produtosUtilizados: List<String>,
    val valorTatuagem: String
)