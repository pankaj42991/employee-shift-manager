package com.pktech.newapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class EmployeeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val isAdmin: Boolean
)
