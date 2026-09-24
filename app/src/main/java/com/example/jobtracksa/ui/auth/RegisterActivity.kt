package com.example.jobtracksa.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.data.remote.RetrofitClient
import com.example.jobtracksa.data.remote.model.RegisterRequest
import com.example.jobtracksa.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            validateRegistration()
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateRegistration() {

        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Full name is required"
            binding.etFullName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            binding.etEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email address"
            binding.etEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            binding.etPassword.requestFocus()
            return
        }

        if (password.length < 8) {
            binding.etPassword.error =
                "Password must be at least 8 characters"
            binding.etPassword.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error =
                "Please confirm your password"
            binding.etConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error =
                "Passwords do not match"
            binding.etConfirmPassword.requestFocus()
            return
        }

        registerUser(fullName, email, password)
    }

    private fun registerUser(
        fullName: String,
        email: String,
        password: String
    ) {

        lifecycleScope.launch {

            try {
                binding.btnRegister.isEnabled = false

                val request = RegisterRequest(
                    fullName = fullName,
                    email = email,
                    password = password
                )

                val response =
                    RetrofitClient.apiService.register(request)

                if (response.isSuccessful) {

                    val authResponse = response.body()

                    Toast.makeText(
                        this@RegisterActivity,
                        authResponse?.message ?: "Registration successful",
                        Toast.LENGTH_LONG
                    ).show()

                    // Registration succeeded.
                    // Send user to Login screen.
                    val intent =
                        Intent(this@RegisterActivity, LoginActivity::class.java)

                    startActivity(intent)
                    finish()

                } else {

                    val message = when (response.code()) {
                        409 -> "An account with this email already exists"
                        400 -> "Please check your registration details"
                        else -> "Registration failed"
                    }

                    Toast.makeText(
                        this@RegisterActivity,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@RegisterActivity,
                    "Unable to connect to the server",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                binding.btnRegister.isEnabled = true
            }
        }
    }
}