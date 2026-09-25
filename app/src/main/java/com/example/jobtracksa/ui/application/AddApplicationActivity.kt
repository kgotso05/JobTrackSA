package com.example.jobtracksa.ui.application

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.data.remote.RetrofitClient
import com.example.jobtracksa.data.remote.model.ApplicationRequest
import com.example.jobtracksa.databinding.ActivityAddApplicationBinding
import kotlinx.coroutines.launch
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale

class AddApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddApplicationBinding

    private val statuses = listOf(
        "Saved",
        "Applied",
        "Assessment",
        "Interview",
        "Offer",
        "Accepted",
        "Rejected"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddApplicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupStatusSpinner()
        loadDefaultStatus()

        binding.btnSaveApplication.setOnClickListener {
            validateAndSave()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupStatusSpinner() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statuses
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerStatus.adapter = adapter
    }

    private fun loadDefaultStatus() {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val defaultStatus = preferences.getString(
            "default_status",
            "Saved"
        )

        val position =
            statuses.indexOf(defaultStatus)

        if (position >= 0) {

            binding.spinnerStatus.setSelection(
                position
            )
        }
    }

    private fun validateAndSave() {

        val companyName =
            binding.etCompanyName.text.toString().trim()

        val jobTitle =
            binding.etJobTitle.text.toString().trim()

        val sourceUrl =
            binding.etSourceUrl.text.toString().trim()

        val closingDate =
            binding.etClosingDate.text.toString().trim()

        val dateApplied =
            binding.etDateApplied.text.toString().trim()

        // Company validation
        if (companyName.isEmpty()) {
            binding.etCompanyName.error =
                "Company name is required"

            binding.etCompanyName.requestFocus()
            return
        }

        if (companyName.length < 2) {
            binding.etCompanyName.error =
                "Enter a valid company name"

            binding.etCompanyName.requestFocus()
            return
        }

        // Job title validation
        if (jobTitle.isEmpty()) {
            binding.etJobTitle.error =
                "Job title is required"

            binding.etJobTitle.requestFocus()
            return
        }

        if (jobTitle.length < 2) {
            binding.etJobTitle.error =
                "Enter a valid job title"

            binding.etJobTitle.requestFocus()
            return
        }

        // URL validation
        if (
            sourceUrl.isNotEmpty() &&
            !Patterns.WEB_URL.matcher(sourceUrl).matches()
        ) {
            binding.etSourceUrl.error =
                "Enter a valid URL"

            binding.etSourceUrl.requestFocus()
            return
        }

        // Closing date validation
        if (
            closingDate.isNotEmpty() &&
            !isValidDate(closingDate)
        ) {
            binding.etClosingDate.error =
                "Use YYYY-MM-DD"

            binding.etClosingDate.requestFocus()
            return
        }

        // Date applied validation
        if (
            dateApplied.isNotEmpty() &&
            !isValidDate(dateApplied)
        ) {
            binding.etDateApplied.error =
                "Use YYYY-MM-DD"

            binding.etDateApplied.requestFocus()
            return
        }

        saveApplication(
            companyName,
            jobTitle
        )
    }

    private fun isValidDate(date: String): Boolean {

        return try {

            val format = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )

            format.isLenient = false
            format.parse(date)

            true

        } catch (e: Exception) {

            false
        }
    }
    private fun saveApplication(
        companyName: String,
        jobTitle: String
    ) {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val token = preferences.getString(
            "auth_token",
            null
        )

        if (token == null) {
            Toast.makeText(
                this,
                "Your session has expired. Please log in again.",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        val request = ApplicationRequest(
            companyName = companyName,
            jobTitle = jobTitle,
            location = binding.etLocation.text
                .toString().trim().ifBlank { null },
            sourceUrl = binding.etSourceUrl.text
                .toString().trim().ifBlank { null },
            status = binding.spinnerStatus.selectedItem.toString(),
            closingDate = binding.etClosingDate.text
                .toString().trim().ifBlank { null },
            dateApplied = binding.etDateApplied.text
                .toString().trim().ifBlank { null },
            notes = binding.etNotes.text
                .toString().trim().ifBlank { null }
        )

        lifecycleScope.launch {

            try {

                binding.btnSaveApplication.isEnabled = false

                binding.btnSaveApplication.text =
                    "Saving..."

                                val response =
                    RetrofitClient.apiService.createApplication(
                        authorization = "Bearer $token",
                        request = request
                    )

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@AddApplicationActivity,
                        "Application saved successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    val message = when (response.code()) {
                        400 -> "Please check the application details"
                        401 -> "Your session has expired. Please log in again."
                        else -> "Unable to save application"
                    }

                    Toast.makeText(
                        this@AddApplicationActivity,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@AddApplicationActivity,
                    "Unable to connect to the server",
                    Toast.LENGTH_LONG
                ).show()

            } finally {
                binding.btnSaveApplication.isEnabled = true
                binding.btnSaveApplication.text =
                    "Save Application"
            }
        }
    }
}