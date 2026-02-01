package com.pktech.newapp.pdf

import android.content.Context
import android.os.Environment
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.data.local.entity.CompOffEntity
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object PdfGenerator {

    fun generateMonthlyEmployeeReport(
        context: Context,
        employeeName: String,
        month: String,
        shifts: List<ShiftEntity>,
        compOff: CompOffEntity?
    ): File {

        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "reports"
        )
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "Shift_Report_${employeeName}_$month.pdf")

        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        document.add(
            Paragraph("PkTech – Employee Shift Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(18f)
        )

        document.add(Paragraph("Employee: $employeeName"))
        document.add(Paragraph("Month: $month"))
        document.add(Paragraph("Generated on: ${LocalDate.now()}"))
        document.add(Paragraph("\n"))

        val table = Table(floatArrayOf(2f, 3f, 3f, 2f))
        table.addHeaderCell("Date")
        table.addHeaderCell("Shift")
        table.addHeaderCell("Holiday")
        table.addHeaderCell("Comp-Off Used")

        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        shifts.forEach {
            table.addCell(it.date.format(formatter))
            table.addCell(it.shiftType)
            table.addCell(if (it.isHoliday) "YES" else "NO")
            table.addCell(if (it.isCompOffUsed) "YES" else "NO")
        }

        document.add(table)
        document.add(Paragraph("\n"))

        document.add(
            Paragraph(
                "Comp-Off Summary:\n" +
                        "Earned: ${compOff?.earned ?: 0}\n" +
                        "Used: ${compOff?.used ?: 0}\n" +
                        "Carry Forward: ${compOff?.carryForward ?: 0}"
            ).setBold()
        )

        document.close()
        return file
    }

    fun generateAdminTeamReport(
        context: Context,
        month: String,
        allShifts: Map<String, List<ShiftEntity>>
    ): File {

        val dir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "reports"
        )
        if (!dir.exists()) dir.mkdirs()

        val file = File(dir, "Team_Shift_Report_$month.pdf")

        val writer = PdfWriter(file)
        val pdfDoc = PdfDocument(writer)
        val document = Document(pdfDoc)

        document.add(
            Paragraph("PkTech – Full Team Shift Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(18f)
        )

        document.add(Paragraph("Month: $month"))
        document.add(Paragraph("\n"))

        allShifts.forEach { (employeeName, shifts) ->
            document.add(
                Paragraph("Employee: $employeeName")
                    .setBold()
                    .setFontSize(14f)
            )

            val table = Table(floatArrayOf(2f, 3f, 3f))
            table.addHeaderCell("Date")
            table.addHeaderCell("Shift")
            table.addHeaderCell("Holiday")

            shifts.forEach {
                table.addCell(it.date.toString())
                table.addCell(it.shiftType)
                table.addCell(if (it.isHoliday) "YES" else "NO")
            }

            document.add(table)
            document.add(Paragraph("\n"))
        }

        document.close()
        return file
    }
}