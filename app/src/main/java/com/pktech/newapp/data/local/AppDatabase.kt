package com.pktech.newapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pktech.newapp.data.local.dao.*
import com.pktech.newapp.data.local.entity.*
import androidx.room.TypeConverters
import com.pktech.newapp.data.local.converter.DateConverter

@Database(
    entities = [
        EmployeeEntity::class,
        ShiftEntity::class,
        HolidayEntity::class,
        CompOffEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase()
abstract class AppDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun shiftDao(): ShiftDao
    abstract fun holidayDao(): HolidayDao
    abstract fun compOffDao(): CompOffDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "newapp_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
