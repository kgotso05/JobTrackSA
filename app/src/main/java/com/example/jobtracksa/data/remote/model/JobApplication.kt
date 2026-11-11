package com.example.jobtracksa.data.remote.model

data class JobApplication(
    val application_id: String,
    val user_id: String,
    val company_name: String,
    val job_title: String,
    val location: String?,
    val source_url: String?,
    val status: String,
    val closing_date: String?,
    val date_applied: String?,
    val notes: String?,
    val created_at: String?,
    val updated_at: String?
)