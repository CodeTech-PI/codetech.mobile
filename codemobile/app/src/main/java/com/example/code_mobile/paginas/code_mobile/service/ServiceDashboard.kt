package com.example.code_mobile.paginas.code_mobile.service

import com.example.code_mobile.paginas.code_mobile.model.ModelAgendamento
import com.example.code_mobile.paginas.code_mobile.model.ModelFaturamento
import com.example.code_mobile.paginas.code_mobile.model.ModelProduto
import retrofit2.Response
import retrofit2.http.GET

interface ServiceDashboard {

    @GET("faturamentos")
    suspend fun getFaturamentos(): Response<List<ModelFaturamento>>

    @GET("agendamentos")
    suspend fun getAgendamentos(): Response<List<ModelAgendamento>>

    @GET("faturamentos")
    suspend fun getFaturamento(): Response<List<ModelFaturamento>>

    @GET("produtos")
    suspend fun getProdutos(): Response<List<ModelProduto>>

    @GET("produtos")
    suspend fun fetchData4(): Response<List<ModelProduto>>
}