package com.example.jobtracksa.data.remote.model

data class ApplicationsListResponse(
    val success: Boolean,
    val applications: List<JobApplication>
)