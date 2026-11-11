package com.example.jobtracksa.util

import java.text.SimpleDateFormat
import java.util.Locale

object ValidationUtils {

    fun isValidCompanyName(name: String): Boolean {
        return name.trim().length >= 2
    }

    fun isValidJobTitle(title: String): Boolean {
        return title.trim().length >= 2
    }

    fun isValidEmail(email: String): Boolean {

        val emailRegex =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"

        return email.matches(
            Regex(emailRegex)
        )
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun passwordsMatch(
        password: String,
        confirmPassword: String
    ): Boolean {

        return password == confirmPassword
    }

    fun isValidDate(date: String): Boolean {

        if (date.isBlank()) {
            return true
        }

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

    fun isValidUrl(url: String): Boolean {

        if (url.isBlank()) {
            return true
        }

        return try {

            val parsedUrl = java.net.URL(url)

            parsedUrl.protocol == "http" ||
                    parsedUrl.protocol == "https"

        } catch (e: Exception) {

            false
        }
    }
}