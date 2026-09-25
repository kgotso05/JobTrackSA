package com.example.jobtracksa.data.remote.model

data class ApplicationResponse(
    val success: Boolean,
    val message: String?,
    val application: JobApplication?
)