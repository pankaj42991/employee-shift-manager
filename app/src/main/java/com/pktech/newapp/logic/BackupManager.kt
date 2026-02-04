package com.pktech.newapp.logic

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pktech.newapp.NewAppApplication
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
class BackupManager(
    private val repository: EmployeeRepository,
    private val context: Context
) {

    private val gson = Gson()

    suspend fun exportBackup(): File = withContext(Dispatchers.IO) {

        val backupData = BackupData(
            employees = repository.getAllEmployees(),
            shifts = repository.getAllShifts(),
            holidays = repository.getAllHolidays(),
            compOffs = repository.getAllCompOffs()
        )

        val json = gson.toJson(backupData)

        val dir = File(context.filesDir, "backups")
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "backup_${System.currentTimeMillis()}.json")
        file.writeText(json)
        file
    }

    suspend fun restoreBackup(file: File) = withContext(Dispatchers.IO) {
        val type = object : TypeToken<BackupData>() {}.type
        val backupData: BackupData = gson.fromJson(file.readText(), type)

        repository.clearAll()
        repository.restoreBackup(backupData)
    }
}
