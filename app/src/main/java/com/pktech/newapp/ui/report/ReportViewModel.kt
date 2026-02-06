package com.pktech.newapp.ui.report

import androidx.lifecycle.*
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.data.repository.EmployeeRepository
import kotlinx.coroutines.launch

class ReportViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    private val _allShifts = MutableLiveData<List<ShiftEntity>>()
    val allShifts: LiveData<List<ShiftEntity>> = _allShifts

    fun loadAllShifts() {
        viewModelScope.launch {
            _allShifts.postValue(
                repository.getAllShifts()
            )
        }
    }
}
