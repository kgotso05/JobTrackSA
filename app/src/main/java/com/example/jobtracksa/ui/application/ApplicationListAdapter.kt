package com.example.jobtracksa.ui.application

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.jobtracksa.R
import com.example.jobtracksa.data.remote.model.JobApplication

class ApplicationListAdapter(
    context: Context,
    applications: List<JobApplication>
) : ArrayAdapter<JobApplication>(
    context,
    0,
    applications
) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(
                R.layout.item_application,
                parent,
                false
            )

        val application = getItem(position)

        val tvJobTitle =
            view.findViewById<TextView>(R.id.tvJobTitle)

        val tvCompany =
            view.findViewById<TextView>(R.id.tvCompany)

        val tvLocation =
            view.findViewById<TextView>(R.id.tvLocation)

        val tvStatus =
            view.findViewById<TextView>(R.id.tvStatus)

        tvJobTitle.text =
            application?.job_title ?: "Unknown position"

        tvCompany.text =
            application?.company_name ?: "Unknown company"

        tvLocation.text =
            application?.location ?: "Location not specified"

        tvStatus.text =
            application?.status ?: "Unknown"

        return view
    }
}