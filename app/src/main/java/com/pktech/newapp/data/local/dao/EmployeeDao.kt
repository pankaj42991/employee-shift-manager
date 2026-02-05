package com.pktech.newapp.data.local.dao

import androidx.room.*
import com.pktech.newapp.data.local.entity.EmployeeEntity

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(employee: EmployeeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(employees: List<EmployeeEntity>)

    @Query("SELECT * FROM employees")
    suspend fun getAll(): List<EmployeeEntity>

    @Query("SELECT * FROM employees WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): EmployeeEntity?

    @Query("DELETE FROM employees")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertAll(list: List<EmployeeEntity>)
}
