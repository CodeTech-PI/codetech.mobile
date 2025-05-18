package com.example.code_mobile.aplicacao_mobile.cModel

import java.math.BigDecimal


data class ModelFaturamento (
    val id : Int,
    val lucro : BigDecimal,
    val idOrdemServico: Int

)