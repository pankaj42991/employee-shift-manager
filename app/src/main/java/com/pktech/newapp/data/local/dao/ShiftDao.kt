package com.pktech.newapp.data.local.dao

import androidx.room.*
import com.pktech.newapp.data.local.entity.ShiftEntity

@Dao
interface ShiftDao {

    @Query("SELECT * FROM shifts")
    suspend fun getAll(): List<ShiftEntity>

    @Query("""
        SELECT * FROM shifts 
        WHERE employeeId = :employeeId 
        AND date LIKE :month || '%'
    """)
    suspend fun getByEmployeeAndMonth(
        employeeId: Int,
        month: String
    ): List<ShiftEntity>

    @Query("""
        SELECT * FROM shifts 
        WHERE date LIKE :month || '%'
    """)
    suspend fun getByMonth(month: String): List<ShiftEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShiftEntity>)

    @Query("DELETE FROM shifts")
    suspend fun clearAll()
}
