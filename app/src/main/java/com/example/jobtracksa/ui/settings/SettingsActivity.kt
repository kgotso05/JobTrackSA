package com.example.jobtracksa.ui.settings

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jobtracksa.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

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
            ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupStatusSpinner()
        loadSettings()

        binding.btnSaveSettings.setOnClickListener {
            saveSettings()
        }

        binding.btnBackSettings.setOnClickListener {
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

        binding.spinnerDefaultStatus.adapter = adapter
    }

    private fun loadSettings() {

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        val email = preferences.getString(
            "user_email",
            "Unknown"
        )

        val defaultStatus = preferences.getString(
            "default_status",
            "Saved"
        )

        val notificationsEnabled =
            preferences.getBoolean(
                "notifications_enabled",
                true
            )

        binding.tvSettingsEmail.text = email

        val position =
            statuses.indexOf(defaultStatus)

        if (position >= 0) {
            binding.spinnerDefaultStatus.setSelection(
                position
            )
        }

        binding.switchNotifications.isChecked =
            notificationsEnabled
    }

    private fun saveSettings() {

        val selectedStatus =
            binding.spinnerDefaultStatus
                .selectedItem
                .toString()

        val notificationsEnabled =
            binding.switchNotifications.isChecked

        val preferences = getSharedPreferences(
            "jobtrack_preferences",
            MODE_PRIVATE
        )

        preferences.edit()
            .putString(
                "default_status",
                selectedStatus
            )
            .putBoolean(
                "notifications_enabled",
                notificationsEnabled
            )
            .apply()

        Toast.makeText(
            this,
            "Settings saved successfully",
            Toast.LENGTH_SHORT
        ).show()
    }
}