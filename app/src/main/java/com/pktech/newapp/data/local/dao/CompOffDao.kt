package com.pktech.newapp.data.local.dao

import androidx.room.*
import com.pktech.newapp.data.local.entity.CompOffEntity

@Dao
interface CompOffDao {

    @Insert
    suspend fun insert(compOff: CompOffEntity)

    @Query("SELECT * FROM comp_off WHERE employeeId = :employeeId")
    suspend fun getByEmployee(employeeId: Int): CompOffEntity?
    
    @Query("SELECT * FROM shifts")
suspend fun getAll(): List<ShiftEntity>

@Query("DELETE FROM shifts")
suspend fun clearAll()
}