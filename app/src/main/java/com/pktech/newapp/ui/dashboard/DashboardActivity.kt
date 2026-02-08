package com.pktech.newapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.R
import com.pktech.newapp.databinding.ActivityDashboardBinding
import com.pktech.newapp.ui.calendar.CalendarFragment
import com.pktech.newapp.ui.home.HomeFragment
import com.pktech.newapp.ui.profile.ProfileFragment
import com.pktech.newapp.ui.report.ReportFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> loadFragment(HomeFragment())
                R.id.menu_calendar -> loadFragment(CalendarFragment())
                R.id.menu_report -> loadFragment(ReportFragment())
                R.id.menu_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
