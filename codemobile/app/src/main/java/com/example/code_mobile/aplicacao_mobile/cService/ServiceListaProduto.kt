package com.example.code_mobile.aplicacao_mobile.cService

import com.example.code_mobile.aplicacao_mobile.cModel.ModelListaProduto
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceListaProduto {

    @GET("lista-produtos/{id}")
    suspend fun encontrarPorId(@Path("id") id: Int): Response<ModelListaProduto>

    @DELETE("lista-produtos/{id}")
    suspend fun deletarListaProduto(@Path("id") id: Int): Response<Unit>

    @POST("lista-produtos")
    suspend fun postListaProduto(@Body requestBody: RequestBody): Response<ResponseBody> // Ou a sua Response DTO

    @PUT("lista-produtos/{id}")
    suspend fun putListaProduto(@Path("id") id: Int, @Body novaListaProduto: ModelListaProduto): Response<Unit>
}