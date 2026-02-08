package com.pktech.newapp.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.R
import com.pktech.newapp.databinding.ActivityDashboardBinding
import com.pktech.newapp.logic.GoogleDriveManager
import com.pktech.newapp.ui.calendar.CalendarFragment
import com.pktech.newapp.ui.home.HomeFragment
import com.pktech.newapp.ui.profile.ProfileFragment
import com.pktech.newapp.ui.report.ReportFragment
import java.io.File

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var driveManager: GoogleDriveManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        driveManager = GoogleDriveManager(this)

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

        setupTopActions()
    }

    private fun setupTopActions() {
        binding.btnBackup?.setOnClickListener {
            startBackup()
        }

        binding.btnRestore?.setOnClickListener {
            confirmRestore()
        }

        binding.btnLogout?.setOnClickListener {
            confirmLogout()
        }
    }

    // ---------------- BACKUP ----------------

    private fun startBackup() {
        if (!driveManager.isSignedIn()) {
            Toast.makeText(this, "Please login with Google first", Toast.LENGTH_LONG).show()
            return
        }

        val file = File(filesDir, "backup.json")

        val dialog = AlertDialog.Builder(this)
            .setTitle("Uploading Backup")
            .setMessage("Please wait...")
            .setCancelable(false)
            .create()

        dialog.show()

        driveManager.uploadBackup(file) { success, msg ->
            dialog.dismiss()
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    // ---------------- RESTORE ----------------

    private fun confirmRestore() {
        AlertDialog.Builder(this)
            .setTitle("Restore Backup")
            .setMessage("This will overwrite current data. Continue?")
            .setPositiveButton("Yes") { _, _ ->
                startRestore()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startRestore() {
        val file = File(filesDir, "restore.json")

        val dialog = AlertDialog.Builder(this)
            .setTitle("Restoring Backup")
            .setMessage("Please wait...")
            .setCancelable(false)
            .create()

        dialog.show()

        driveManager.downloadLatestBackup(file) { success, msg ->
            dialog.dismiss()
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    // ---------------- LOGOUT ----------------

    private fun confirmLogout() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Do you really want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        GoogleSignIn.getClient(this, GoogleSignIn.getLastSignedInAccount(this)?.account?.let {
            com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder(
                com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
            ).build()
        } ?: return).signOut()

        finish()
    }

    // ---------------- FRAGMENT LOADER ----------------

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
