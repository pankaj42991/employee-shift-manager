package com.pktech.newapp.logic

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File as DriveFile
import com.google.api.client.http.FileContent
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.util.Collections

class GoogleDriveManager(private val context: Context) {

    private var driveService: Drive? = null

    init {
        val account = GoogleSignIn.getLastSignedInAccount(context)

        account?.let {
            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                Collections.singleton(DriveScopes.DRIVE_FILE)
            ).setSelectedAccount(it.account)

            driveService = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            ).setApplicationName("Employee Shift Manager")
                .build()
        }
    }

    fun isSignedIn(): Boolean = driveService != null

    // ---------------- UPLOAD BACKUP ----------------

    fun uploadBackup(file: File, callback: (Boolean, String) -> Unit) {
        val service = driveService ?: return callback(false, "Please sign in first")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val metadata = DriveFile().setName(file.name)
                val content = FileContent("application/octet-stream", file)

                service.files().create(metadata, content).execute()

                withContext(Dispatchers.Main) {
                    callback(true, "Backup uploaded to Google Drive")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(false, e.message ?: "Upload failed")
                }
            }
        }
    }

    // ---------------- DOWNLOAD & RESTORE ----------------

    fun downloadLatestBackup(localFile: File, callback: (Boolean, String) -> Unit) {
        val service = driveService ?: return callback(false, "Please sign in first")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val files = service.files().list()
                    .setQ("name contains 'backup'")
                    .setSpaces("drive")
                    .setFields("files(id, name, modifiedTime)")
                    .execute()

                if (files.files.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        callback(false, "No backup found")
                    }
                    return@launch
                }

                val latest = files.files.maxBy { it.modifiedTime.value }

                val output = FileOutputStream(localFile)
                service.files().get(latest.id).executeMediaAndDownloadTo(output)

                withContext(Dispatchers.Main) {
                    callback(true, "Backup restored successfully")
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback(false, e.message ?: "Restore failed")
                }
            }
        }
    }
}
