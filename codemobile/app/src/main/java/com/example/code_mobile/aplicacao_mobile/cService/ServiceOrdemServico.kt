package com.example.code_mobile.aplicacao_mobile.cService

import com.example.code_mobile.aplicacao_mobile.cModel.ModelOrdemServico
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceOrdemServico {

    @GET("ordens-servicos")
    suspend fun listarOrdensServico(): Response<List<ModelOrdemServico>>

    @GET("ordens-servicos/{id}")
    suspend fun buscarOrdemServicoPorId(@Path("id") id: Int): Response<ModelOrdemServico>

    @POST("ordens-servicos")
    suspend fun criarOrdemServico(@Body novaOrdemServico: ModelOrdemServico): Response<ModelOrdemServico>

    @PUT("ordens-servicos/{id}")
    suspend fun atualizarOrdemServico(
        @Path("id") id: Int,
        @Body ordemServicoRequestDto: ModelOrdemServico
    ): Response<ModelOrdemServico>

    @GET("ordens-servicos/buscar-preco/{valorProcurado}")
    suspend fun buscarOrdemServicoPorPreco(@Path("valorProcurado") valorProcurado: Double): Response<List<ModelOrdemServico>>

}