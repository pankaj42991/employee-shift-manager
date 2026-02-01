package com.pktech.newapp.ui.home

import androidx.lifecycle.*
import com.pktech.newapp.data.repository.EmployeeRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    private val _employeeCount = MutableLiveData<Int>()
    val employeeCount: LiveData<Int> = _employeeCount

    private val _shiftCount = MutableLiveData<Int>()
    val shiftCount: LiveData<Int> = _shiftCount

    fun loadDashboardData() {
        viewModelScope.launch {
            _employeeCount.postValue(repository.getEmployeeCount())
            _shiftCount.postValue(repository.getShiftCount())
        }
    }
}