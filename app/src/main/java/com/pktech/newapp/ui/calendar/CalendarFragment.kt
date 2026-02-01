package com.pktech.newapp.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pktech.newapp.NewAppApplication
import com.pktech.newapp.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.YearMonth

class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels {
        CalendarViewModelFactory(
            (requireActivity().application as NewAppApplication)
                .employeeRepository
        )
    }

    private lateinit var adapter: CalendarAdapter
    private var currentMonth: YearMonth = YearMonth.now()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // Setup RecyclerView as calendar grid
        adapter = CalendarAdapter()
        binding.rvCalendar.layoutManager = GridLayoutManager(context, 7)
        binding.rvCalendar.adapter = adapter

        // Load calendar for current month
        loadMonth(currentMonth)

        // Prev / Next month buttons
        binding.btnPrev.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            loadMonth(currentMonth)
        }

        binding.btnNext.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            loadMonth(currentMonth)
        }

        return binding.root
    }

    private fun loadMonth(month: YearMonth) {
        viewModel.getShiftsForMonth(month).observe(viewLifecycleOwner) { shiftList ->
            adapter.submitList(shiftList)
            binding.tvMonth.text = month.month.name + " " + month.year
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}