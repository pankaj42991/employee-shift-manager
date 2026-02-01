package com.pktech.newapp.ui.report

import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.databinding.FragmentReportBinding
import com.pktech.newapp.data.local.entity.ShiftEntity
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportFragment : Fragment(R.layout.fragment_report) {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportViewModel by viewModels {
        ReportViewModelFactory(
            (requireActivity().application as NewAppApplication)
                .employeeRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)

        binding.btnGenerate.setOnClickListener {
            generatePdfReport()
        }

        return binding.root
    }

    private fun generatePdfReport() {
        viewModel.getAllShifts().observe(viewLifecycleOwner) { shifts ->
            if (shifts.isEmpty()) {
                Toast.makeText(requireContext(), "No shifts found", Toast.LENGTH_SHORT).show()
                return@observe
            }

            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            var y = 50
            canvas.drawText("Shift Report", 40f, y.toFloat(), android.graphics.Paint().apply {
                textSize = 20f
            })
            y += 40

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            shifts.forEach { shift ->
                val line = "${shift.employeeId} | ${shift.date.format(formatter)} | ${shift.shiftType} | Comp-Off Used: ${shift.isCompOffUsed}"
                canvas.drawText(line, 40f, y.toFloat(), android.graphics.Paint().apply { textSize = 14f })
                y += 25
            }

            pdfDocument.finishPage(page)

            try {
                val file = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "Shift_Report_${System.currentTimeMillis()}.pdf"
                )
                pdfDocument.writeTo(FileOutputStream(file))
                Toast.makeText(requireContext(), "PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Failed to save PDF", Toast.LENGTH_SHORT).show()
            }

            pdfDocument.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
})