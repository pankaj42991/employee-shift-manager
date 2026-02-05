package com.pktech.newapp.ui.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.data.repository.EmployeeRepository
import com.pktech.newapp.pdf.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ReportViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    fun getAllShifts() = repository.getAllShifts()

    fun generateAdminMonthlyReport(
        context: Context,
        month: String,
        onResult: (File) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shifts = repository.getAdminMonthlyReport(month)

                val grouped = shifts.groupBy {
                    it.date.substring(0, 7) // yyyy-MM
                }

                val pdf = PdfGenerator.generateAdminMonthlyReport(
                    context,
                    month,
                    grouped
                )
                onResult(pdf)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
