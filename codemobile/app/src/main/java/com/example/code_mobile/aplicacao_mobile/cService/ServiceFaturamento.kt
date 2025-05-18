package com.example.code_mobile.aplicacao_mobile.cService

import com.example.code_mobile.aplicacao_mobile.cModel.ModelFaturamento;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface ServiceFaturamento {
    @GET("/faturamentos/{id}")
    suspend fun listarFaturamento(@Path("id") id: Int): Response<ModelFaturamento>
}