package com.pktech.newapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pktech.newapp.data.local.entity.CompOffEntity

@Dao
interface CompOffDao {

    @Query("SELECT * FROM comp_offs")
    suspend fun getAll(): List<CompOffEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<CompOffEntity>)

    @Query("DELETE FROM comp_offs")
    suspend fun clearAll()
}
