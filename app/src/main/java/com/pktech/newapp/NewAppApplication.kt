package com.pktech.newapp

import android.app.Application
import com.pktech.newapp.data.local.AppDatabase
import com.pktech.newapp.data.repository.EmployeeRepository

class NewAppApplication : Application() {

    // Room Database
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    // Single source of truth Repository
    val employeeRepository: EmployeeRepository by lazy {
        EmployeeRepository(
            employeeDao = database.employeeDao(),
            shiftDao = database.shiftDao(),
            holidayDao = database.holidayDao(),
            compOffDao = database.compOffDao(),
            shiftAssignmentDao = database.shiftAssignmentDao()
        )
    }
}
