package com.pktech.newapp.pdf

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.data.local.entity.CompOffEntity
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object PdfGenerator {

    private val titlePaint = Paint().apply {
        textSize = 18f
        isFakeBoldText = true
    }

    private val normalPaint = Paint().apply {
        textSize = 12f
    }

    private val boldPaint = Paint().apply {
        textSize = 12f
        isFakeBoldText = true
    }

    // =========================
    // EMPLOYEE MONTHLY REPORT
    // =========================
    fun generateMonthlyEmployeeReport(
        context: Context,
        employeeName: String,
        month: String,
        shifts: List<ShiftEntity>,
        compOff: CompOffEntity?
    ): File {

        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40f

        canvas.drawText("PkTech – Employee Shift Report", 40f, y, titlePaint)
        y += 30

        canvas.drawText("Employee: $employeeName", 40f, y, normalPaint)
        y += 18
        canvas.drawText("Month: $month", 40f, y, normalPaint)
        y += 18
        canvas.drawText("Generated on: ${LocalDate.now()}", 40f, y, normalPaint)
        y += 30

        // Table Header
        canvas.drawText("Date", 40f, y, boldPaint)
        canvas.drawText("Shift", 140f, y, boldPaint)
        canvas.drawText("Holiday", 260f, y, boldPaint)
        canvas.drawText("Comp-Off", 380f, y, boldPaint)
        y += 15

        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        shifts.forEach {
            canvas.drawText(it.date.format(formatter), 40f, y, normalPaint)
            canvas.drawText(it.shiftType, 140f, y, normalPaint)
            canvas.drawText(if (it.isHoliday) "YES" else "NO", 260f, y, normalPaint)
            canvas.drawText(if (it.isCompOffUsed) "YES" else "NO", 380f, y, normalPaint)
            y += 14
        }

        y += 25

        canvas.drawText("Comp-Off Summary", 40f, y, boldPaint)
        y += 16
        canvas.drawText("Earned: ${compOff?.earned ?: 0}", 40f, y, normalPaint)
        y += 14
        canvas.drawText("Used: ${compOff?.used ?: 0}", 40f, y, normalPaint)
        y += 14
        canvas.drawText("Carry Forward: ${compOff?.carryForward ?: 0}", 40f, y, normalPaint)

        pdf.finishPage(page)

        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "reports"
        )
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "Shift_Report_${employeeName}_$month.pdf")
        pdf.writeTo(FileOutputStream(file))
        pdf.close()

        return file
    }

    // =========================
    // ADMIN TEAM REPORT
    // =========================
    fun generateAdminTeamReport(
        context: Context,
        month: String,
        allShifts: Map<String, List<ShiftEntity>>
    ): File {

        val pdf = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdf.startPage(pageInfo)
        val canvas = page.canvas

        var y = 40f

        canvas.drawText("PkTech – Full Team Shift Report", 40f, y, titlePaint)
        y += 25
        canvas.drawText("Month: $month", 40f, y, normalPaint)
        y += 30

        allShifts.forEach { (employee, shifts) ->

            canvas.drawText("Employee: $employee", 40f, y, boldPaint)
            y += 16

            shifts.forEach {
                canvas.drawText(
                    "${it.date} | ${it.shiftType} | Holiday: ${if (it.isHoliday) "YES" else "NO"}",
                    40f,
                    y,
                    normalPaint
                )
                y += 14
            }

            y += 20
        }

        pdf.finishPage(page)

        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "reports"
        )
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "Team_Shift_Report_$month.pdf")
        pdf.writeTo(FileOutputStream(file))
        pdf.close()

        return file
    }
    // PdfGenerator.kt ke andar LAST me add karo

fun generateEmployeeMonthlyReport(
    context: Context,
    employeeName: String,
    month: String,
    shifts: List<ShiftEntity>
): File {
    return generateMonthlyEmployeeReport(
        context,
        employeeName,
        month,
        shifts,
        compOff = null
    )
}

fun generateAdminMonthlyReport(
    context: Context,
    month: String,
    data: Map<String, List<ShiftEntity>>
): File {
    return generateAdminTeamReport(context, month, data)
}
}
