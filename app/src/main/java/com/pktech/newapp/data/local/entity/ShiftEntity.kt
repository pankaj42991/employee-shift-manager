package com.pktech.newapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "shifts",
    indices = [Index(value = ["employeeId"])],
    foreignKeys = [
        ForeignKey(
            entity = EmployeeEntity::class,
            parentColumns = ["id"],
            childColumns = ["employeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: Int,
    val date: LocalDate,
    val shiftType: String,
    val isHoliday: Boolean,
    val isCompOffUsed: Boolean
)
