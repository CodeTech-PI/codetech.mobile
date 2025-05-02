package com.example.code_mobile.paginas.code_mobile.cService

import com.example.code_mobile.paginas.code_mobile.cModel.ModelCategoria
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceCategoria {

    // Listar todas
    @GET("categorias")
    suspend fun getCategorias(): Response<List<ModelCategoria>>

    // Buscar por ID
    @GET("categorias/{id}")
    suspend fun getCategoriaById(@Path("id") id: Int): Response<ModelCategoria>

    // Criar nova
    @POST("categorias")
    suspend fun criarCategoria(@Body categoria: ModelCategoria): Response<ModelCategoria>

    // Atualizar
    @PUT("categorias/{id}")
    suspend fun atualizarCategoria(
        @Path("id") id: Int,
        @Body categoria: ModelCategoria
    ): Response<ModelCategoria>

    // Deletar
    @DELETE("categorias/{id}")
    suspend fun deletarCategoria(@Path("id") id: Int): Response<Void>


}