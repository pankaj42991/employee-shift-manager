package com.pktech.newapp.auth

import com.google.firebase.auth.FirebaseAuth
import com.pktech.newapp.data.repository.EmployeeRepository
import com.pktech.newapp.data.local.entity.EmployeeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val repository: EmployeeRepository
) {

    private val auth = FirebaseAuth.getInstance()

    suspend fun syncUserWithLocalDb() = withContext(Dispatchers.IO) {
        val firebaseUser = auth.currentUser ?: return@withContext
        val email = firebaseUser.email ?: return@withContext
        val name = firebaseUser.displayName ?: email.substringBefore("@")

        val existing = repository.getEmployeeByEmail(email)

        if (existing == null) {
            repository.insertEmployee(
                EmployeeEntity(
                    name = name,
                    email = email,
                    isAdmin = email.equals("pankaj@gmail.com", true)
                )
            )
        }
    }
}    }

    /**
     * Decide Admin based on email
     * (Simple + transparent + future editable)
     */
    private fun isAdminEmail(email: String): Boolean {
        return email.equals("pankaj@gmail.com", ignoreCase = true)
    }
}
