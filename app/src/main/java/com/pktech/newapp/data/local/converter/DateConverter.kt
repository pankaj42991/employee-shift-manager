package com.pktech.newapp.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? =
        date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }
}
