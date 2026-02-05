package com.pktech.newapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.R
import com.pktech.newapp.auth.AuthActivity
import com.pktech.newapp.logic.BackupManager
import com.pktech.newapp.logic.GoogleDriveManager
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var backupManager: BackupManager
    private lateinit var driveManager: GoogleDriveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as NewAppApplication

        backupManager = BackupManager(
            repository = app.employeeRepository,
            context = requireContext()
        )

        driveManager = GoogleDriveManager(requireContext())

        view.findViewById<View>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }

        view.findViewById<View>(R.id.btnBackup).setOnClickListener {
            lifecycleScope.launch {
                val file = backupManager.exportBackup()
                Toast.makeText(requireContext(), "Backup: ${file.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
