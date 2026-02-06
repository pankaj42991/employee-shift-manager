package com.pktech.newapp.logic

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File as DriveFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Collections

class GoogleDriveManager(private val context: Context) {

    private var googleDriveService: Drive? = null
    // Background tasks ke liye scope
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        setupDriveService()
    }

    private fun setupDriveService() {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            // DRIVE_FILE scope: App sirf apni banayi files dekh payegi (Safe & Free)
            val credential = GoogleAccountCredential.usingOAuth2(
                context, Collections.singleton(DriveScopes.DRIVE_FILE)
            ).setSelectedAccount(account.account)

            googleDriveService = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            ).setApplicationName("Employee Shift Manager").build()
        }
    }

    /**
     * Backup File Upload Karne Ka Function
     */
    fun uploadBackup(file: File, callback: (Boolean, String) -> Unit) {
        val service = googleDriveService
        if (service == null) {
            callback(false, "Google Drive signed in nahi hai!")
            return
        }

        // Coroutine start: Background mein upload hoga
        scope.launch {
            try {
                // 1. File ki details set karein
                val fileMetadata = DriveFile().apply {
                    name = file.name
                    // Agar specific folder me dalna ho toh parents set karein
                    // parents = listOf("appDataFolder") 
                }

                // 2. File ka content taiyar karein
                val mediaContent = com.google.api.client.http.FileContent("application/octet-stream", file)

                // 3. Upload command execute karein
                val driveFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute()

                // 4. Success message main thread par bhein
                withContext(Dispatchers.Main) {
                    if (driveFile.id != null) {
                        callback(true, "Backup safaltapurvak upload ho gaya! ID: ${driveFile.id}")
                    } else {
                        callback(false, "Upload toh hua par ID nahi mili.")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    callback(false, "Galti: ${e.localizedMessage}")
                }
            }
        }
    }
}
