package com.example.code_mobile.paginas.code_mobile.cModel

import java.math.BigDecimal

data class ModelEstoque (
    val id: Int?,
    val nome: String,
    val descricao: String,
    val unidadeMedida: String,
    val preco: BigDecimal,
    val quantidade: Int,
    val categoria: ModelCategoria
)