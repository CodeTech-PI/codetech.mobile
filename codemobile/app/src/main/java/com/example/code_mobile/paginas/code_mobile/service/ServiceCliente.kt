package com.example.code_mobile.paginas.code_mobile.service


import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceCliente {
    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<ModelCliente>>

    @GET("usuarios/{id}")
    suspend fun encontrarPorId(@Path("id") id: Int): Response<ModelCliente>

    @DELETE("usuarios/{id}")
    suspend fun deletarUsuario(@Path("id") id: Int): Response<Unit> // Void =  Unit

    @POST("usuarios")
    suspend fun postUsuario(@Body novoCliente: ModelCliente): Response<Unit>

    @PUT("usuarios/{id}")
    suspend fun putUsuario(@Path("id") id: Int, @Body novoCliente: ModelCliente): Response<Unit>

}