package com.pktech.newapp.ui.report

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pktech.newapp.App
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.pdf.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ReportViewModel : ViewModel() {

    /**
     * Monthly PDF for single employee
     * month format: yyyy-MM  (example: 2026-01)
     */
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
                val shiftDao = NewAppApplication.db.shiftDao()

                val shifts: List<ShiftEntity> =
                    shiftDao.getByEmployeeAndMonth(employeeId, month)

                val pdfFile = PdfGenerator.generateEmployeeMonthlyReport(
                    context = context,
                    employeeName = employeeName,
                    month = month,
                    shifts = shifts
                )

                onResult(pdfFile)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**
     * Monthly PDF for admin (all employees)
     */
    fun generateAdminMonthlyReport(
        context: Context,
        month: String,
        onResult: (File) -> Unit,
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val employeeDao = NewAppApplication.db.employeeDao()
                val shiftDao = NewAppApplication.db.shiftDao()

                val employees = employeeDao.getAll()
                val reportData = LinkedHashMap<String, List<ShiftEntity>>()

                employees.forEach { emp ->
                    val shifts = shiftDao.getByEmployeeAndMonth(emp.id, month)
                    reportData[emp.name] = shifts
                }

                val pdfFile = PdfGenerator.generateAdminMonthlyReport(
                    context = context,
                    month = month,
                    data = reportData
                )

                onResult(pdfFile)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
