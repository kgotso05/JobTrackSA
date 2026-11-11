package com.example.jobtracksa.ui.application

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.data.remote.RetrofitClient
import com.example.jobtracksa.data.remote.model.ApplicationRequest
import com.example.jobtracksa.data.remote.model.JobApplication
import com.example.jobtracksa.databinding.ActivityEditApplicationBinding
import kotlinx.coroutines.launch
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.Locale

class EditApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditApplicationBinding

    private var applicationId: String? = null

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

        binding =
            ActivityEditApplicationBinding.inflate(layoutInflater)

        setContentView(binding.root)

        applicationId =
            intent.getStringExtra("application_id")

        if (applicationId == null) {
            Toast.makeText(
                this,
                "Application could not be opened",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        setupStatusSpinner()

        applicationId?.let {
            loadApplication(it)
        }

        binding.btnUpdateApplication.setOnClickListener {
            validateAndUpdate()
        }

        binding.btnCancelEdit.setOnClickListener {
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

        binding.spinnerEditStatus.adapter = adapter
    }

    private fun loadApplication(id: String) {

        val token = getToken() ?: return

        lifecycleScope.launch {

            try {

                binding.btnUpdateApplication.isEnabled = false

                val response =
                    RetrofitClient.apiService.getApplicationById(
                        "Bearer $token",
                        id
                    )

                if (response.isSuccessful) {

                    val application =
                        response.body()?.application

                    if (application != null) {
                        populateForm(application)
                    } else {
                        showMessage("Application not found")
                    }

                } else {
                    showMessage("Unable to load application")
                }

            } catch (e: Exception) {

                showMessage(
                    "Unable to connect to the server"
                )

            } finally {

                binding.btnUpdateApplication.isEnabled = true
            }
        }
    }

    private fun populateForm(
        application: JobApplication
    ) {

        binding.etEditCompanyName.setText(
            application.company_name
        )

        binding.etEditJobTitle.setText(
            application.job_title
        )

        binding.etEditLocation.setText(
            application.location ?: ""
        )

        binding.etEditSourceUrl.setText(
            application.source_url ?: ""
        )

        binding.etEditClosingDate.setText(
            application.closing_date ?: ""
        )

        binding.etEditDateApplied.setText(
            application.date_applied ?: ""
        )

        binding.etEditNotes.setText(
            application.notes ?: ""
        )

        val statusPosition =
            statuses.indexOf(application.status)

        if (statusPosition >= 0) {
            binding.spinnerEditStatus.setSelection(
                statusPosition
            )
        }
    }

    private fun validateAndUpdate() {

        val companyName =
            binding.etEditCompanyName.text
                .toString()
                .trim()

        val jobTitle =
            binding.etEditJobTitle.text
                .toString()
                .trim()

        val sourceUrl =
            binding.etEditSourceUrl.text
                .toString()
                .trim()

        val closingDate =
            binding.etEditClosingDate.text
                .toString()
                .trim()

        val dateApplied =
            binding.etEditDateApplied.text
                .toString()
                .trim()

        if (companyName.isEmpty()) {

            binding.etEditCompanyName.error =
                "Company name is required"

            binding.etEditCompanyName.requestFocus()
            return
        }

        if (jobTitle.isEmpty()) {

            binding.etEditJobTitle.error =
                "Job title is required"

            binding.etEditJobTitle.requestFocus()
            return
        }

        updateApplication(
            companyName,
            jobTitle
        )

        if (
            sourceUrl.isNotEmpty() &&
            !Patterns.WEB_URL.matcher(sourceUrl).matches()
        ) {

            binding.etEditSourceUrl.error =
                "Enter a valid URL"

            binding.etEditSourceUrl.requestFocus()
            return
        }

        if (
            closingDate.isNotEmpty() &&
            !isValidDate(closingDate)
        ) {

            binding.etEditClosingDate.error =
                "Use YYYY-MM-DD"

            binding.etEditClosingDate.requestFocus()
            return
        }

        if (
            dateApplied.isNotEmpty() &&
            !isValidDate(dateApplied)
        ) {

            binding.etEditDateApplied.error =
                "Use YYYY-MM-DD"

            binding.etEditDateApplied.requestFocus()
            return
        }
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

    private fun updateApplication(
        companyName: String,
        jobTitle: String
    ) {

        val id = applicationId ?: return
        val token = getToken() ?: return

        val request = ApplicationRequest(
            companyName = companyName,
            jobTitle = jobTitle,

            location =
                binding.etEditLocation.text
                    .toString()
                    .trim()
                    .ifBlank { null },

            sourceUrl =
                binding.etEditSourceUrl.text
                    .toString()
                    .trim()
                    .ifBlank { null },

            status =
                binding.spinnerEditStatus
                    .selectedItem
                    .toString(),

            closingDate =
                binding.etEditClosingDate.text
                    .toString()
                    .trim()
                    .ifBlank { null },

            dateApplied =
                binding.etEditDateApplied.text
                    .toString()
                    .trim()
                    .ifBlank { null },

            notes =
                binding.etEditNotes.text
                    .toString()
                    .trim()
                    .ifBlank { null }
        )

        lifecycleScope.launch {

            try {

                binding.btnUpdateApplication.isEnabled =
                    false
                binding.btnUpdateApplication.text =
                    "Updating..."

                val response =
                    RetrofitClient.apiService.updateApplication(
                        authorization = "Bearer $token",
                        applicationId = id,
                        request = request
                    )

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@EditApplicationActivity,
                        "Application updated successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                }

                else {

                    when (response.code()) {

                        400 ->
                            showMessage(
                                "Please check the application details"
                            )

                        401 ->
                            showMessage(
                                "Your session has expired"
                            )

                        404 ->
                            showMessage(
                                "Application not found"
                            )

                        else ->
                            showMessage(
                                "Unable to update application"
                            )
                    }
                }

            } catch (e: Exception) {

                showMessage(
                    "Unable to connect to the server"
                )

            } finally {

                binding.btnUpdateApplication.isEnabled = true
                binding.btnUpdateApplication.text =
                    "Update Application"
            }
        }
    }

    private fun getToken(): String? {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val token =
            preferences.getString(
                "auth_token",
                null
            )

        if (token == null) {

            showMessage(
                "Your session has expired"
            )

            finish()
        }

        return token
    }

    private fun showMessage(message: String) {

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}