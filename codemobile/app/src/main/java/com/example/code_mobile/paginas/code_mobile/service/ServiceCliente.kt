package com.example.code_mobile.paginas.code_mobile.service


import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceCliente {
    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<ModelCliente>>

    @DELETE("usuarios/{id}")
    suspend fun deletarUsuario(@Path("id") id: Int): Response<Unit> // Void =  Unit

    @POST("usuarios/{id}")
    suspend fun postUsuario(@Path("id") id: Int): Response<Unit> // Void =  Unit

}