package com.example.code_mobile.paginas.code_mobile.service

import com.example.code_mobile.paginas.code_mobile.model.ModelCliente
import com.example.code_mobile.paginas.code_mobile.model.ModelFiliais
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceFiliais {
    @GET("unidades")
    suspend fun getFiliais(): Response<List<ModelFiliais>>

    @GET("unidades/{id}")
    suspend fun getFilialById(@Path("id") id: Int): Response<ModelFiliais>

    @POST("unidades")
    suspend fun createFilial(@Body filial: ModelFiliais): Response<ModelFiliais>

    @PUT("unidades/{id}")
    suspend fun updateFilial(@Path("id") id: Int, @Body filial: ModelFiliais): Response<ModelFiliais>

    @DELETE("unidades/{id}")
    suspend fun deleteFilial(@Path("id") id: Int): Response<Unit>
}