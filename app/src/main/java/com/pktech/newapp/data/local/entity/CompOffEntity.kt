package com.pktech.newapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comp_off")
data class CompOffEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: Int,
    val earned: Int,
    val used: Int,
    val carryForward: Int
)