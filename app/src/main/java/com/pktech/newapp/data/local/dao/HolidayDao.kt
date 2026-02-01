package com.pktech.newapp.data.local.dao

import androidx.room.*
import com.pktech.newapp.data.local.entity.HolidayEntity

@Dao
interface HolidayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holiday: HolidayEntity)

    @Query("SELECT * FROM holidays")
    suspend fun getAll(): List<HolidayEntity>
    
    @Query("SELECT * FROM shifts")
suspend fun getAll(): List<ShiftEntity>

@Query("DELETE FROM shifts")
suspend fun clearAll()
}