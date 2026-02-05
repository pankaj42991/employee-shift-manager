package com.pktech.newapp.auth

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.data.local.entity.EmployeeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val application: Application
) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Sync Firebase user into Room DB
     */
    suspend fun syncUserWithLocalDb() = withContext(Dispatchers.IO) {

        val firebaseUser = auth.currentUser ?: return@withContext

        val email = firebaseUser.email ?: return@withContext
        val name = firebaseUser.displayName ?: email.substringBefore("@")

        val app = application as NewAppApplication
        val employeeDao = app.database.employeeDao()

        val existing = employeeDao.getByEmail(email)

        if (existing == null) {
            employeeDao.insert(
                EmployeeEntity(
                    name = name,
                    email = email,
                    isAdmin = isAdminEmail(email)
                )
            )
        }
    }

    private fun isAdminEmail(email: String): Boolean {
        return email.equals("pankaj@gmail.com", ignoreCase = true)
    }
}
