package com.example.code_mobile.paginas.code_mobile.service

import retrofit2.http.GET
import retrofit2.Response
import com.example.code_mobile.paginas.code_mobile.model.ModelEstoque

interface ServiceEstoque {
    @GET("produtos")
    suspend fun getProdutos(): Response<List<ModelEstoque>>
}