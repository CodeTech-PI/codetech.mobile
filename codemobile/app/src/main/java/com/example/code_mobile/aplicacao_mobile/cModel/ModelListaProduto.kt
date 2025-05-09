package com.example.code_mobile.aplicacao_mobile.cModel

import com.example.code_mobile.paginas.code_mobile.cModel.ModelEstoque

data class ModelListaProduto(
    val id: Int?,
    val quantidade: Int,
    val produto: Int, //idProduto
    val agendamento: Int //idAgendamento
)