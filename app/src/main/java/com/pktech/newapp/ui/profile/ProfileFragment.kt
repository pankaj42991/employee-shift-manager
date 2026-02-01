package com.pktech.newapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.R
import com.pktech.newapp.auth.AuthActivity
import com.pktech.newapp.logic.BackupManager
import com.pktech.newapp.logic.GoogleDriveManager
import kotlinx.coroutines.launch
import java.io.File

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var backupManager: BackupManager
    private lateinit var driveManager: GoogleDriveManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backupManager = BackupManager(requireContext())
        driveManager = GoogleDriveManager(requireContext())

        // ✅ Logout Button
        view.findViewById<android.widget.Button>(R.id.btnLogout).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
            requireActivity().finish()
        }

        // ✅ Local Backup Button
        view.findViewById<android.widget.Button>(R.id.btnBackup).setOnClickListener {
            lifecycleScope.launch {
                val file = backupManager.exportBackup()
                Toast.makeText(
                    requireContext(),
                    "Backup saved locally: ${file.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // ✅ Local Restore Button
        view.findViewById<android.widget.Button>(R.id.btnRestore).setOnClickListener {
            lifecycleScope.launch {
                val dir = requireContext().filesDir.resolve("backups")
                val files = dir.listFiles()?.sortedByDescending { it.lastModified() }
                if (!files.isNullOrEmpty()) {
                    backupManager.restoreBackup(files[0])
                    Toast.makeText(
                        requireContext(),
                        "Backup restored from: ${files[0].name}",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "No backup found!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ✅ Optional: Upload Backup to Google Drive
        view.findViewById<android.widget.Button>(R.id.btnUploadDrive).setOnClickListener {
            lifecycleScope.launch {
                val file = backupManager.exportBackup()
                driveManager.uploadBackup(file) { success, msg ->
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ✅ Optional: Download Backup from Google Drive
        view.findViewById<android.widget.Button>(R.id.btnDownloadDrive).setOnClickListener {
            val file = requireContext().filesDir.resolve("backups/restore_from_drive.json")
            driveManager.downloadBackup(file) { success, msg ->
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}