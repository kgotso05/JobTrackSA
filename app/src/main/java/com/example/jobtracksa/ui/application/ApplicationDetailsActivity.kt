package com.example.jobtracksa.ui.application

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.data.remote.RetrofitClient
import com.example.jobtracksa.data.remote.model.JobApplication
import com.example.jobtracksa.databinding.ActivityApplicationDetailsBinding
import kotlinx.coroutines.launch
import android.content.Intent
import androidx.appcompat.app.AlertDialog

class ApplicationDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplicationDetailsBinding

    private var applicationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityApplicationDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        applicationId = intent.getStringExtra("application_id")

        if (applicationId == null) {
            Toast.makeText(
                this,
                "Application could not be opened",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        binding.btnEditApplication.setOnClickListener {

            val id = applicationId ?: return@setOnClickListener

            val intent = Intent(
                this,
                EditApplicationActivity::class.java
            )

            intent.putExtra(
                "application_id",
                id
            )

            startActivity(intent)
        }

        binding.btnDeleteApplication.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun showDeleteConfirmation() {

        AlertDialog.Builder(this)
            .setTitle("Delete Application")
            .setMessage(
                "Are you sure you want to delete this application? " +
                        "This action cannot be undone."
            )
            .setPositiveButton("Delete") { _, _ ->

                applicationId?.let {
                    deleteApplication(it)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteApplication(id: String) {

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
                "Your session has expired",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        lifecycleScope.launch {

            try {

                binding.btnDeleteApplication.isEnabled = false
                binding.btnEditApplication.isEnabled = false

                val response =
                    RetrofitClient.apiService.deleteApplication(
                        authorization = "Bearer $token",
                        applicationId = id
                    )

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@ApplicationDetailsActivity,
                        "Application deleted successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()

                } else {

                    when (response.code()) {

                        401 -> showError(
                            "Your session has expired"
                        )

                        404 -> showError(
                            "Application not found"
                        )

                        else -> showError(
                            "Unable to delete application"
                        )
                    }
                }

            } catch (e: Exception) {

                showError(
                    "Unable to connect to the server"
                )

            } finally {

                binding.btnDeleteApplication.isEnabled = true
                binding.btnEditApplication.isEnabled = true
            }
        }
    }

    override fun onResume() {
        super.onResume()

        applicationId?.let {
            loadApplication(it)
        }
    }

    private fun loadApplication(id: String) {

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
                "Your session has expired",
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        lifecycleScope.launch {

            try {

                setLoading(true)

                val response =
                    RetrofitClient.apiService.getApplicationById(
                        authorization = "Bearer $token",
                        applicationId = id
                    )

                if (response.isSuccessful) {

                    val application =
                        response.body()?.application

                    if (application != null) {
                        displayApplication(application)
                    } else {
                        showError("Application not found")
                    }

                } else {

                    when (response.code()) {
                        401 ->
                            showError("Your session has expired")

                        404 ->
                            showError("Application not found")

                        else ->
                            showError("Unable to load application")
                    }
                }

            } catch (e: Exception) {

                showError(
                    "Unable to connect to the server"
                )

            } finally {

                setLoading(false)
            }
        }
    }

    private fun displayApplication(
        application: JobApplication
    ) {

        binding.tvDetailJobTitle.text =
            application.job_title

        binding.tvDetailCompany.text =
            application.company_name

        binding.tvDetailStatus.text =
            application.status

        binding.tvDetailLocation.text =
            "Location: ${application.location ?: "Not specified"}"

        binding.tvDetailClosingDate.text =
            "Closing date: ${application.closing_date ?: "Not specified"}"

        binding.tvDetailDateApplied.text =
            "Date applied: ${application.date_applied ?: "Not specified"}"

        binding.tvDetailUrl.text =
            "Application URL: ${application.source_url ?: "Not specified"}"

        binding.tvDetailNotes.text =
            application.notes ?: "No notes"
    }

    private fun setLoading(loading: Boolean) {

        binding.btnEditApplication.isEnabled = !loading
        binding.btnDeleteApplication.isEnabled = !loading
    }

    private fun showError(message: String) {

        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}