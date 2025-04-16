package com.example.code_mobile.paginas.code_mobile.service

import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.paginas.code_mobile.model.ModelEstoque

interface ServiceEstoque {
    @GET("produtos")
    suspend fun getProdutos(): Response<List<ModelEstoque>>

    // TODO: Adicionar endpoints para cadastrar, editar e excluir produtos
    // @POST("produtos")
    // suspend fun cadastrarProduto(@Body novoProduto: ModelEstoque): Response<ModelEstoque>
    //
    // @PUT("produtos/{id}")
    // suspend fun editarProduto(@Path("id") id: Int, @Body produtoAtualizado: ModelEstoque): Response<ModelEstoque>
    //
    // @DELETE("produtos/{id}")
    // suspend fun excluirProduto(@Path("id") id: Int): Response<Unit>
}