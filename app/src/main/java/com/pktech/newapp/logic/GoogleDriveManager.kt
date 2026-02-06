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
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        account?.let {
            val credential = GoogleAccountCredential.usingOAuth2(
                context, Collections.singleton(DriveScopes.DRIVE_FILE)
            ).setSelectedAccount(it.account)

            googleDriveService = Drive.Builder(
                AndroidHttp.newCompatibleTransport(),
                GsonFactory.getDefaultInstance(),
                credential
            ).setApplicationName("EmployeeShiftManager").build()
        }
    }

    fun uploadBackup(file: File, callback: (Boolean, String) -> Unit) {
        val service = googleDriveService ?: return callback(false, "Please Sign In first")
        scope.launch {
            try {
                val metadata = DriveFile().setName(file.name)
                val content = com.google.api.client.http.FileContent("application/octet-stream", file)
                service.files().create(metadata, content).execute()
                withContext(Dispatchers.Main) { callback(true, "Backup Uploaded!") }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { callback(false, "Error: ${e.message}") }
            }
        }
    }
}
