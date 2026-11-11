package com.example.jobtracksa.data.remote.model

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)