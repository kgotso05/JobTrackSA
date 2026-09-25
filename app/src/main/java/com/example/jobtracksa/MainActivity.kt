package com.example.jobtracksa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobtracksa.databinding.ActivityMainBinding
import com.example.jobtracksa.ui.auth.LoginActivity
import com.example.jobtracksa.ui.application.AddApplicationActivity
import com.example.jobtracksa.ui.application.ApplicationsActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import com.example.jobtracksa.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAuthentication()
        setupDashboard()
        setupClickListeners()
    }

    private fun checkAuthentication() {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val token = preferences.getString("auth_token", null)

        if (token == null) {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }

    private fun setupDashboard() {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val email = preferences.getString(
            "user_email",
            null
        )

        if (email != null) {
            binding.tvWelcome.text = "Welcome back!"
        }
    }

    private fun setupClickListeners() {

        binding.btnAddApplication.setOnClickListener {

            val intent = Intent(
                this,
                AddApplicationActivity::class.java
            )

            startActivity(intent)
        }

        binding.btnViewApplications.setOnClickListener {

            val intent = Intent(
                this,
                ApplicationsActivity::class.java
            )

            startActivity(intent)
        }

        binding.btnSettings.setOnClickListener {

            val intent = Intent(
                this,
                SettingsActivity::class.java
            )

            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun goToLogin() {

        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()
    }
    private fun logout() {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        preferences.edit()
            .remove("auth_token")
            .remove("user_email")
            .apply()

        Toast.makeText(
            this,
            "Logged out successfully",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(
            this,
            LoginActivity::class.java
        )

        // Clear authenticated screens from back stack
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun loadDashboardStatistics() {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val token = preferences.getString(
            "auth_token",
            null
        )

        if (token == null) {
            goToLogin()
            return
        }

        lifecycleScope.launch {

            try {

                val response =
                    RetrofitClient.apiService.getApplications(
                        "Bearer $token"
                    )

                if (response.isSuccessful) {

                    val applications =
                        response.body()?.applications ?: emptyList()

                    val total = applications.size

                    val saved = applications.count {
                        it.status.equals(
                            "Saved",
                            ignoreCase = true
                        )
                    }

                    val applied = applications.count {
                        it.status.equals(
                            "Applied",
                            ignoreCase = true
                        )
                    }

                    val interview = applications.count {
                        it.status.equals(
                            "Interview",
                            ignoreCase = true
                        )
                    }

                    val offers = applications.count {
                        it.status.equals(
                            "Offer",
                            ignoreCase = true
                        )
                    }

                    binding.tvTotalApplications.text =
                        total.toString()

                    binding.tvSaved.text =
                        saved.toString()

                    binding.tvApplied.text =
                        applied.toString()

                    binding.tvInterview.text =
                        interview.toString()

                    binding.tvOffers.text =
                        offers.toString()

                } else if (response.code() == 401) {

                    Toast.makeText(
                        this@MainActivity,
                        "Your session has expired",
                        Toast.LENGTH_LONG
                    ).show()

                    logout()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@MainActivity,
                    "Unable to update dashboard",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loadDashboardStatistics()
    }
}