package com.example.code_mobile.token.network

/* Biblioteca para realizar requisições HTTP */
import com.example.code_mobile.paginas.code_mobile.service.ServiceDashboard
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofithAuth {
    private const val BASE_URL = "http://192.168.17.152:8080/"


    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .build()

    // No objeto RetrofithAuth:
    public val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(
            GsonBuilder()
                .setDateFormat("yyyy-MM-dd") // Formato da data
                .create()
        ))
        .build()
    val dashboardService: ServiceDashboard by lazy {
        retrofit.create(ServiceDashboard::class.java)
    }
}

/*
    retrofit = Configura instância para comunicação com o nosso back-end
    OkHttpClient  = Adiciona o token automaticamente aos headers de todas as requisições feitas pela instância do retrofit
 */