package com.pktech.newapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.App
import com.pktech.newapp.data.local.entity.EmployeeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    /**
     * Sync Firebase user into Room DB
     * Creates employee if not exists
     */
    suspend fun syncUserWithLocalDb() = withContext(Dispatchers.IO) {
        val firebaseUser = auth.currentUser ?: return@withContext

        val email = firebaseUser.email ?: return@withContext
        val name = firebaseUser.displayName ?: email.substringBefore("@")

        val employeeDao = App.db.employeeDao()
        val existing = employeeDao.getByEmail(email)

        if (existing == null) {
            val isAdmin = isAdminEmail(email)

            employeeDao.insert(
                EmployeeEntity(
                    name = name,
                    email = email,
                    isAdmin = isAdmin
                )
            )
        }
    }

    /**
     * Decide Admin based on email
     * (Simple + transparent + future editable)
     */
    private fun isAdminEmail(email: String): Boolean {
        return email.equals("pankaj@gmail.com", ignoreCase = true)
    }
}