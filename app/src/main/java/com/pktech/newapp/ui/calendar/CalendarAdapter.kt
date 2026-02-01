package com.pktech.newapp.ui.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pktech.newapp.data.local.entity.ShiftEntity
import com.pktech.newapp.databinding.ItemCalendarDayBinding
import java.time.format.DateTimeFormatter

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    private val days = mutableListOf<ShiftEntity>()

    fun submitList(list: List<ShiftEntity>) {
        days.clear()
        days.addAll(list)
        notifyDataSetChanged()
    }

    inner class DayViewHolder(val binding: ItemCalendarDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(shift: ShiftEntity) {
            binding.tvDay.text = shift.date.dayOfMonth.toString()

            // ✅ Color coding
            binding.tvDay.setBackgroundColor(
                when {
                    shift.isHoliday -> Color.parseColor("#FFC0CB") // pink for holiday
                    shift.isCompOffUsed -> Color.parseColor("#90EE90") // light green for comp-off
                    else -> Color.WHITE
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(days[position])
    }

    override fun getItemCount(): Int = days.size
}