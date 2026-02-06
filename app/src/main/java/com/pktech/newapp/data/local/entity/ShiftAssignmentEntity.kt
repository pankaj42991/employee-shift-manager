package com.pktech.newapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "shift_assignments")
data class ShiftAssignmentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val employeeId: Int,

    val shiftId: Int,

    val date: LocalDate
)
