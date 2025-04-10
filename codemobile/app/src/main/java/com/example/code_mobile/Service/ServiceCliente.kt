package com.example.code_mobile.Service


import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.Model.ModelCliente

interface ServiceCliente {
    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<ModelCliente>>
}