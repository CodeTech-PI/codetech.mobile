package com.example.code_mobile.token.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenManager.token

        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", token)
            println("TOKEN ENVIADO: $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}