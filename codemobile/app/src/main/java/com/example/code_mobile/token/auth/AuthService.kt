package com.example.code_mobile.token.auth

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

data class LoginRequest(val email: String, val senha: String)
data class LoginResponse(val token: String)

interface AuthService {
    @POST("lombardi/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

/*
    Envia a requisição de login para o backend
*/