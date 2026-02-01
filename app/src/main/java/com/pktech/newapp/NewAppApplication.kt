package com.pktech.newapp

import android.app.Application
import com.pktech.newapp.data.local.AppDatabase
import com.pktech.newapp.data.repository.EmployeeRepository

class NewAppApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val employeeRepository by lazy {
        EmployeeRepository(
            database.employeeDao(),
            database.shiftDao()
        )
    }
}