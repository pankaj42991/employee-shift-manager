package com.pktech.newapp.data.repository

import com.pktech.newapp.data.local.dao.EmployeeDao
import com.pktech.newapp.data.local.dao.ShiftDao

class EmployeeRepository(
    private val employeeDao: EmployeeDao,
    private val shiftDao: ShiftDao
) {

    suspend fun getEmployeeCount(): Int {
        return employeeDao.getAll().size
    }

    suspend fun getShiftCount(): Int {
        return shiftDao.getAll().size
    }
}