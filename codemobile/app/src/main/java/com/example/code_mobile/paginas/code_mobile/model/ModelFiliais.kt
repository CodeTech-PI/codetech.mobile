package com.example.code_mobile.paginas.code_mobile.model

data class ModelFiliais(
    val id: Int?,
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val complemento: String,
    val num: Int,
    val status: String
)
