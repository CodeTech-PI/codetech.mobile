package com.example.code_mobile.paginas.code_mobile.service

import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.paginas.code_mobile.model.ModelEstoque
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceEstoque {
    @GET("produtos")
    suspend fun getProdutos(): Response<List<ModelEstoque>>

    @GET("produtos/{id}")
    suspend fun getProdutoPorId(@Path("id") id: Int): Response<ModelEstoque>

    @POST("produtos")
    suspend fun postProduto(@Body novoProduto: ModelEstoque): Response<ModelEstoque>

    @PUT("produtos/{id}")
    suspend fun putProduto(@Path("id") id: Int, @Body produtoAtualizado: ModelEstoque): Response<ModelEstoque>
    //
    @DELETE("produtos/{id}")
    suspend fun deleteProduto(@Path("id") id: Int): Response<Unit>

}