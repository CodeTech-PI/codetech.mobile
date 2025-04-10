package com.example.code_mobile.paginas.code_mobile.Cliente

import com.example.code_mobile.paginas.code_mobile.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response

interface InterfaceCliente {

    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<ModelCliente>> // Retorna uma lista de objetos Usuario dentro de um Response

    // Objeto singleton que configura o Retrofit
    object RetrofitClient {
        private const val BASE_URL = "http://10.18.33.26:8080/"

        private val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsb21iYXJkaUBsb2NhbGhvc3QiLCJpYXQiOjE3NDQyNDM3NjgsImV4cCI6MTc0Nzg0Mzc2OH0.QxZ6P06Is28jlqSliaanraM8DFIZLyaMNP6ANr3LxOz65kkhM10Av7OHOrLVQ4AgIkcrJoYT74MdO2RP_sG3fQ" // depois você troca isso dinamicamente

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()

        val api: InterfaceCliente by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(InterfaceCliente::class.java)
        }
    }

}