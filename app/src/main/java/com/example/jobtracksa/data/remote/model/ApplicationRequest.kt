package com.example.jobtracksa.data.remote.model

data class ApplicationRequest(
    val companyName: String,
    val jobTitle: String,
    val location: String?,
    val sourceUrl: String?,
    val status: String,
    val closingDate: String?,
    val dateApplied: String?,
    val notes: String?
)