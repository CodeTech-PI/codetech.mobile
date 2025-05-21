package com.example.code_mobile.aplicacao_mobile.cService

import android.view.Display.Mode
import com.example.code_mobile.aplicacao_mobile.cModel.ModelAgendamento
import com.example.code_mobile.paginas.code_mobile.cModel.ModelCliente
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface  ServiceAgendamento {

    @GET("agendamentos")
    suspend fun getAgendamentos(): Response<List<ModelAgendamento>>

    @GET("agendamentos/{id}")
    suspend fun encontrarPorId(@Path("id") id: Int): Response<ModelAgendamento>

    @DELETE("agendamentos/{id}")
    suspend fun deletarAgendamento(@Path("id") id: Int): Response<Unit>

    @POST("agendamentos")
    suspend fun postAgendamento(@Body novoAgendamento: ModelAgendamento): Response<ModelAgendamento>

    @PUT("agendamentos/{id}")
    suspend fun putAgendamento(@Path("id") id: Int, @Body novoAgendamento: ModelAgendamento): Response<Unit>
}