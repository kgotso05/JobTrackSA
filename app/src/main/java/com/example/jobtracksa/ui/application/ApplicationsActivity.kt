package com.example.jobtracksa.ui.application

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jobtracksa.data.remote.RetrofitClient
import com.example.jobtracksa.data.remote.model.JobApplication
import com.example.jobtracksa.databinding.ActivityApplicationsBinding
import kotlinx.coroutines.launch

class ApplicationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplicationsBinding

    private val applications =
        mutableListOf<JobApplication>()

    private lateinit var adapter: ApplicationListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            ActivityApplicationsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        adapter = ApplicationListAdapter(
            this,
            applications
        )

        binding.listApplications.adapter = adapter

        binding.listApplications.setOnItemClickListener {
                _,
                _,
                position,
                _ ->

            val application = applications[position]

            val intent = android.content.Intent(
                this,
                ApplicationDetailsActivity::class.java
            )

            intent.putExtra(
                "application_id",
                application.application_id
            )

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        loadApplications()
    }

    private fun loadApplications() {

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

                binding.progressBar.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.GONE

                val response =
                    RetrofitClient.apiService.getApplications(
                        "Bearer $token"
                    )

                if (response.isSuccessful) {

                    val result = response.body()

                    applications.clear()

                    if (result != null) {
                        applications.addAll(
                            result.applications
                        )
                    }

                    adapter.notifyDataSetChanged()

                    binding.tvApplicationCount.text =
                        if (applications.size == 1) {
                            "1 application"
                        } else {
                            "${applications.size} applications"
                        }

                    if (applications.isEmpty()) {
                        binding.tvEmpty.visibility =
                            View.VISIBLE
                    }

                } else {

                    when (response.code()) {

                        401 -> Toast.makeText(
                            this@ApplicationsActivity,
                            "Your session has expired",
                            Toast.LENGTH_LONG
                        ).show()

                        else -> Toast.makeText(
                            this@ApplicationsActivity,
                            "Unable to load applications",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@ApplicationsActivity,
                    "Unable to connect to the server",
                    Toast.LENGTH_LONG
                ).show()

            } finally {

                binding.progressBar.visibility =
                    View.GONE
            }
        }
    }
}