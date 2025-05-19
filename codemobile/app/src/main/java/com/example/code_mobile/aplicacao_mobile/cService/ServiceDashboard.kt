package com.example.code_mobile.paginas.code_mobile.cService

import com.example.code_mobile.paginas.code_mobile.cModel.ModelAgendamentoDash
import com.example.code_mobile.paginas.code_mobile.cModel.ModelFaturamentoDash
import com.example.code_mobile.paginas.code_mobile.cModel.ModelProduto
import retrofit2.Response
import retrofit2.http.GET

interface ServiceDashboard {

    @GET("faturamentos")
    suspend fun getFaturamentos(): Response<List<ModelFaturamentoDash>>

    @GET("agendamentos")
    suspend fun getAgendamentos(): Response<List<ModelAgendamentoDash>>

    @GET("faturamentos")
    suspend fun getFaturamento(): Response<List<ModelFaturamentoDash>>

    @GET("produtos")
    suspend fun getProdutos(): Response<List<ModelProduto>>

    @GET("produtos")
    suspend fun fetchData4(): Response<List<ModelProduto>>
}