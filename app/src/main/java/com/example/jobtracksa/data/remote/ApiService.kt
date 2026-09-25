package com.example.jobtracksa.data.remote

import com.example.jobtracksa.data.remote.model.AuthResponse
import com.example.jobtracksa.data.remote.model.LoginRequest
import com.example.jobtracksa.data.remote.model.RegisterRequest
import com.example.jobtracksa.data.remote.model.ApplicationRequest
import com.example.jobtracksa.data.remote.model.ApplicationResponse
import retrofit2.http.Header
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.jobtracksa.data.remote.model.ApplicationsListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE
import com.example.jobtracksa.data.remote.model.DeleteApplicationResponse

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/applications")
    suspend fun createApplication(
        @Header("Authorization") authorization: String,
        @Body request: ApplicationRequest
    ): Response<ApplicationResponse>

    @GET("api/applications")
    suspend fun getApplications(
        @Header("Authorization") authorization: String
    ): Response<ApplicationsListResponse>

    @GET("api/applications/{id}")
    suspend fun getApplicationById(
        @Header("Authorization") authorization: String,
        @Path("id") applicationId: String
    ): Response<ApplicationResponse>

    @PUT("api/applications/{id}")
    suspend fun updateApplication(
        @Header("Authorization") authorization: String,
        @Path("id") applicationId: String,
        @Body request: ApplicationRequest
    ): Response<ApplicationResponse>

    @DELETE("api/applications/{id}")
    suspend fun deleteApplication(
        @Header("Authorization") authorization: String,
        @Path("id") applicationId: String
    ): Response<DeleteApplicationResponse>

}