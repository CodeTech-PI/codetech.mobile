package com.example.code_mobile.paginas.code_mobile.model

data class ModelEstoque (
    val id: Int,
    val nome: String,
    val descricao: String,
    val unidadeMedida: String,
    val preco: Double,
    val quantidade: Int,
    val categoria: String = "Teste"
)