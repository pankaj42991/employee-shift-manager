package com.pktech.newapp.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pktech.newapp.data.repository.EmployeeRepository

class ReportViewModelFactory(
    private val repository: EmployeeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReportViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}