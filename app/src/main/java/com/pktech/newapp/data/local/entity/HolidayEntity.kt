package com.pktech.newapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey val date: LocalDate,
    val name: String
)