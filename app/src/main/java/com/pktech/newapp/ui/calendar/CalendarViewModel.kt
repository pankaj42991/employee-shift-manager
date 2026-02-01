package com.pktech.newapp.ui.calendar

import androidx.lifecycle.*
import com.pktech.newapp.data.repository.EmployeeRepository
import com.pktech.newapp.data.local.entity.ShiftEntity
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    fun getShiftsForMonth(month: YearMonth): LiveData<List<ShiftEntity>> {
        val result = MutableLiveData<List<ShiftEntity>>()
        viewModelScope.launch {
            val monthStr = "${month.year}-${month.monthValue.toString().padStart(2, '0')}"
            val allShifts = mutableListOf<ShiftEntity>()
            for (day in 1..month.lengthOfMonth()) {
                val date = LocalDate.of(month.year, month.monthValue, day)
                val shifts = repository.getShiftsForDate(date)
                allShifts.addAll(shifts)
            }
            result.postValue(allShifts)
        }
        return result
    }
}