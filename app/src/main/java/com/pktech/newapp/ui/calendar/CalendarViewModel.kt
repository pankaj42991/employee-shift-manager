package com.pktech.newapp.ui.calendar

import androidx.lifecycle.*
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.data.repository.EmployeeRepository
import kotlinx.coroutines.launch
import java.time.YearMonth

class CalendarViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    fun getShiftsForMonth(month: YearMonth): LiveData<List<ShiftEntity>> {
        val liveData = MutableLiveData<List<ShiftEntity>>()

        viewModelScope.launch {
            val monthStr = "${month.year}-${month.monthValue.toString().padStart(2, '0')}"
            val shifts = repository.getShiftsByMonth(monthStr)
            liveData.postValue(shifts)
        }

        return liveData
    }
}
