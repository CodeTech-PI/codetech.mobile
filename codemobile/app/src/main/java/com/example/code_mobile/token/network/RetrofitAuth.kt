package com.example.code_mobile.token.network

/* Biblioteca para realizar requisições HTTP */
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofithAuth {
    private const val BASE_URL = "http://192.168.15.18:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

/*
    retrofit = Configura instância para comunicação com o nosso back-end
    OkHttpClient  = Adiciona o token automaticamente aos headers de todas as requisições feitas pela instância do retrofit
 */