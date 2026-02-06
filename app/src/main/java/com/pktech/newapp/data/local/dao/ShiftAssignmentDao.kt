package com.pktech.newapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pktech.newapp.data.local.entity.ShiftAssignmentEntity
import java.time.LocalDate

@Dao
interface ShiftAssignmentDao {

    @Query("SELECT * FROM shift_assignments")
    suspend fun getAll(): List<ShiftAssignmentEntity>

    @Query("SELECT * FROM shift_assignments WHERE date = :date")
    suspend fun getByDate(date: LocalDate): List<ShiftAssignmentEntity>

    // ✅ YE FUNCTION ADD KARNA ZARURI HAI (Build error yahi se aa raha hai)
    @Query("SELECT * FROM shift_assignments WHERE date LIKE :month || '%'")
    suspend fun getByMonth(month: String): List<ShiftAssignmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShiftAssignmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ShiftAssignmentEntity>)

    @Query("DELETE FROM shift_assignments")
    suspend fun clearAll()
}
