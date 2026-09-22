package com.example.jobtracksa.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import com.example.jobtracksa.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("RegisterActivity", "Registration screen opened")

        binding.registerButton.setOnClickListener {
            validateRegistration()
        }

        binding.loginTextView.setOnClickListener {
            Log.d("RegisterActivity", "Returning to login screen")

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun validateRegistration() {

        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()
        val confirmPassword =
            binding.confirmPasswordEditText.text.toString()

        clearErrors()

        var isValid = true

        if (name.isEmpty()) {
            binding.nameInputLayout.error = "Full name is required"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.emailInputLayout.error = "Email is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInputLayout.error = "Enter a valid email address"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordInputLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 8) {
            binding.passwordInputLayout.error =
                "Password must contain at least 8 characters"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordInputLayout.error =
                "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordInputLayout.error =
                "Passwords do not match"
            isValid = false
        }

        if (isValid) {
            Log.d(
                "RegisterActivity",
                "Registration input validated successfully",
            )

            // REST API registration will be added later.
        }
    }

    private fun clearErrors() {
        binding.nameInputLayout.error = null
        binding.emailInputLayout.error = null
        binding.passwordInputLayout.error = null
        binding.confirmPasswordInputLayout.error = null
    }
}