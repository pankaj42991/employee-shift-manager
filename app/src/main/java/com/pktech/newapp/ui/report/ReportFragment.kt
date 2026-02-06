package com.pktech.newapp.ui.report

import android.graphics.Paint
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
import com.pktech.newapp.R
import com.pktech.newapp.databinding.FragmentReportBinding
import java.io.File
import java.io.FileOutputStream
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
            viewModel.loadAllShifts()
        }

        observeData()

        return binding.root
    }

    private fun observeData() {
        viewModel.allShifts.observe(viewLifecycleOwner) { shifts ->

            if (shifts.isEmpty()) {
                Toast.makeText(requireContext(), "No shifts found", Toast.LENGTH_SHORT).show()
                return@observe
            }

            val pdf = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdf.startPage(pageInfo)
            val canvas = page.canvas

            val titlePaint = Paint().apply { textSize = 20f }
            val textPaint = Paint().apply { textSize = 14f }

            var y = 50
            canvas.drawText("Shift Report", 40f, y.toFloat(), titlePaint)
            y += 40

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            shifts.forEach { shift ->
                val line =
                    "Emp:${shift.employeeId}  Date:${shift.date.format(formatter)}  Shift:${shift.shiftType}"
                canvas.drawText(line, 40f, y.toFloat(), textPaint)
                y += 24
            }

            pdf.finishPage(page)

            try {
                val file = File(
                    requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    "Shift_Report_${System.currentTimeMillis()}.pdf"
                )
                pdf.writeTo(FileOutputStream(file))
                Toast.makeText(requireContext(), "PDF saved:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "PDF save failed", Toast.LENGTH_SHORT).show()
            }

            pdf.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
