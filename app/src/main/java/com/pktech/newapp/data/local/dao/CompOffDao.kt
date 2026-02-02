package com.pktech.newapp.data.local.dao

import androidx.room.*
import com.pktech.newapp.data.local.entity.CompOffEntity

@Dao
interface CompOffDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(compOff: CompOffEntity)

    @Query("SELECT * FROM comp_off WHERE employeeId = :employeeId")
    suspend fun getByEmployee(employeeId: Int): CompOffEntity?

    @Query("SELECT * FROM comp_off")
    suspend fun getAll(): List<CompOffEntity>

    @Query("DELETE FROM comp_off")
    suspend fun clearAll()
}
