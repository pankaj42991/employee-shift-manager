package com.pktech.newapp.logic

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pktech.newapp.App
import com.pktech.newapp.data.local.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class BackupData(
    val employees: List<EmployeeEntity>,
    val shifts: List<ShiftEntity>,
    val holidays: List<HolidayEntity>,
    val compOffs: List<CompOffEntity>
)

class BackupManager(private val context: Context) {

    private val gson = Gson()

    /**
     * Export full Room DB to JSON
     */
    suspend fun exportBackup(): File = withContext(Dispatchers.IO) {
        val employees = NewAppApplication.db.employeeDao().getAll()
        val shifts = NewAppApplication.db.shiftDao().getAll()
        val holidays = NewAppApplication.db.holidayDao().getAll()
        val compOffs = NewAppApplication.db.compOffDao().getAll()

        val backupData = BackupData(employees, shifts, holidays, compOffs)
        val json = gson.toJson(backupData)

        val dir = File(context.filesDir, "backups")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "backup_${System.currentTimeMillis()}.json")
        file.writeText(json)
        return@withContext file
    }

    /**
     * Restore Room DB from JSON file
     */
    suspend fun restoreBackup(file: File) = withContext(Dispatchers.IO) {
        val json = file.readText()
        val type = object : TypeToken<BackupData>() {}.type
        val backupData: BackupData = gson.fromJson(json, type)

        val db = App.db

        // Clear existing data (optional)
        db.employeeDao().clearAll()
        db.shiftDao().clearAll()
        db.holidayDao().clearAll()
        db.compOffDao().clearAll()

        // Restore data
        backupData.employees.forEach { db.employeeDao().insert(it) }
        backupData.shifts.forEach { db.shiftDao().insert(it) }
        backupData.holidays.forEach { db.holidayDao().insert(it) }
        backupData.compOffs.forEach { db.compOffDao().insert(it) }
    }
}
