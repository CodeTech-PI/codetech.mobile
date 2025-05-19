package com.example.code_mobile.paginas.code_mobile.cModel

data class ModelProduto(
    val id: Int,
    val quantidade: Int,
    val nome: String,
    val descricao: String,
    val unidadeMedida: String,
    val preco: Double, // Ou BigDecimal se precisar de precisão monetária
    val categoria: ModelCategoria
) {
    data class ModelCategoria(
        val id: Int,
        val nome: String
    )
}