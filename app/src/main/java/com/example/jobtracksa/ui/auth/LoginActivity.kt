package com.example.jobtracksa.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.MainActivity
import com.example.jobtracksa.data.remote.RetrofitClient
import com.example.jobtracksa.data.remote.model.LoginRequest
import com.example.jobtracksa.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            validateLogin()
        }

        binding.tvRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }

    private fun validateLogin() {

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) {

            binding.etEmail.error =
                "Email is required"

            binding.etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            binding.etEmail.error =
                "Enter a valid email address"

            binding.etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {

            binding.etPassword.error =
                "Password is required"

            binding.etPassword.requestFocus()
            return
        }

        if (password.length < 8) {
            binding.etPassword.error =
                "Password must be at least 8 characters"
            binding.etPassword.requestFocus()
            return
        }

        loginUser(email, password)
    }

    private fun loginUser(
        email: String,
        password: String
    ) {

        lifecycleScope.launch {

            try {

                binding.btnLogin.isEnabled = false

                val request = LoginRequest(
                    email = email,
                    password = password
                )

                val response =
                    RetrofitClient.apiService.login(request)

                if (response.isSuccessful) {

                    val authResponse = response.body()

                    if (authResponse != null &&
                        authResponse.success &&
                        authResponse.token != null
                    ) {

                        // Store JWT locally
                        val preferences = getSharedPreferences(
                            "jobtrack_preferences",
                            MODE_PRIVATE
                        )

                        preferences.edit()
                            .putString(
                                "auth_token",
                                authResponse.token
                            )
                            .putString(
                                "user_email",
                                authResponse.user?.email
                            )
                            .apply()

                        Toast.makeText(
                            this@LoginActivity,
                            "Login successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Open the main part of the app
                        val intent = Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )

                        startActivity(intent)

                        // User cannot return to login using Back
                        finish()

                    } else {

                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                } else {

                    val message = when (response.code()) {

                        401 ->
                            "Invalid email or password"

                        400 ->
                            "Email and password are required"

                        else ->
                            "Login failed"
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@LoginActivity,
                    "Unable to connect to the server",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                binding.btnLogin.isEnabled = true
            }
        }
    }
}