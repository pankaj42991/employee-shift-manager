package com.pktech.newapp.data.local.dao

import androidx.room.*
import com.pktech.newapp.data.local.entity.ShiftEntity
import java.time.LocalDate

@Dao
interface ShiftDao {

    @Insert
    suspend fun insert(shift: ShiftEntity)

    @Query("SELECT * FROM shifts WHERE date = :date")
    suspend fun getByDate(date: LocalDate): List<ShiftEntity>
    

    @Query("""
        SELECT * FROM shifts
        WHERE employeeId = :employeeId
        AND strftime('%Y-%m', date) = :month
    """)
    suspend fun getByEmployeeAndMonth(
        employeeId: Int,
        month: String   // example: 2026-01
    ): List<ShiftEntity>
    
    @Query("SELECT * FROM shifts")
suspend fun getAll(): List<ShiftEntity>

@Query("DELETE FROM shifts")
suspend fun clearAll()

@Query("SELECT * FROM shifts WHERE date LIKE :month || '%'")
suspend fun getByMonth(month: String): List<ShiftEntity>

@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertAll(list: List<ShiftEntity>)
}
