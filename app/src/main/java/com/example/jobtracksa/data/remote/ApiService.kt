package com.example.jobtracksa.data.remote

import com.example.jobtracksa.data.remote.model.AuthResponse
import com.example.jobtracksa.data.remote.model.LoginRequest
import com.example.jobtracksa.data.remote.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>
}