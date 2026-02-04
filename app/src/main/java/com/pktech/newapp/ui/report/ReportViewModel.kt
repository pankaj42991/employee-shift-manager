 package com.pktech.newapp.ui.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.pdf.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ReportViewModel(
    private val repository: EmployeeRepository
) : ViewModel() {

    fun generateEmployeeMonthlyReport(
        context: Context,
        employeeId: Int,
        employeeName: String,
        month: String,
        onResult: (File) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val shifts = repository.getShiftsByEmployeeAndMonth(employeeId, month)

                val pdfFile = PdfGenerator.generateEmployeeMonthlyReport(
                    context,
                    employeeName,
                    month,
                    shifts
                )

                onResult(pdfFile)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun generateAdminMonthlyReport(
        context: Context,
        month: String,
        onResult: (File) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = repository.getAdminMonthlyReport(month)
                val pdfFile = PdfGenerator.generateAdminMonthlyReport(context, month, data)
                onResult(pdfFile)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
