package com.example.code_mobile.service


import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.model.ModelCliente

interface ServiceCliente {
    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<ModelCliente>>
}