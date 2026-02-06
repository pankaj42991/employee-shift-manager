package com.pktech.newapp.data.repository

import com.pktech.newapp.data.local.dao.*
import com.pktech.newapp.data.local.entity.*
import com.pktech.newapp.logic.BackupData
import java.time.LocalDate


class EmployeeRepository(
    private val employeeDao: EmployeeDao,
    private val shiftDao: ShiftDao,
    private val holidayDao: HolidayDao,
    private val compOffDao: CompOffDao,
    private val shiftAssignmentDao: ShiftAssignmentDao
) {

    // Employees
    suspend fun getAllEmployees(): List<EmployeeEntity> = employeeDao.getAll()
    suspend fun getEmployeeCount(): Int = employeeDao.getAll().size

    // Shifts
    suspend fun getAllShifts(): List<ShiftEntity> = shiftDao.getAll()
    suspend fun getShiftCount(): Int = shiftDao.getAll().size
    suspend fun getShiftsByEmployeeAndMonth(employeeId: Int, month: String): List<ShiftEntity> =
        shiftDao.getByEmployeeAndMonth(employeeId, month)

    suspend fun getShiftsByMonth(month: String): List<ShiftEntity> =
        shiftDao.getByMonth(month)

            // ---------------- SHIFT ASSIGNMENTS ----------------

    suspend fun assignShift(entity: ShiftAssignmentEntity) =
        shiftAssignmentDao.insert(entity)

    suspend fun getAssignmentsByMonth(month: String): List<ShiftAssignmentEntity> =
        shiftAssignmentDao.getByMonth(month)

    // Holidays
    suspend fun getAllHolidays(): List<HolidayEntity> = holidayDao.getAll()

    // CompOffs
    suspend fun getAllCompOffs(): List<CompOffEntity> = compOffDao.getAll()

    // Backup & Restore
    suspend fun clearAll() {
        employeeDao.clearAll()
        shiftDao.clearAll()
        holidayDao.clearAll()
        compOffDao.clearAll()
    }

    suspend fun restoreBackup(data: com.pktech.newapp.logic.BackupData) {
        employeeDao.insertAll(data.employees)
        shiftDao.insertAll(data.shifts)
        holidayDao.insertAll(data.holidays)
        compOffDao.insertAll(data.compOffs)
    }

    // Admin report
    suspend fun getAdminMonthlyReport(month: String): List<ShiftEntity> =
        shiftDao.getByMonth(month)
}
