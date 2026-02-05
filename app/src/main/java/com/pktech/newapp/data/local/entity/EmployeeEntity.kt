package com.pktech.newapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,   // ✅ auto-generate fix

    val name: String,
    val email: String,
    val isAdmin: Boolean
)
