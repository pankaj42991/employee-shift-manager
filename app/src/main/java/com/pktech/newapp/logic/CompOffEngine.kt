package com.pktech.newapp.logic

import com.pktech.newapp.data.local.entity.CompOffEntity
import com.pktech.newapp.data.local.entity.ShiftEntity
import java.time.DayOfWeek
import java.time.LocalDate

class CompOffEngine {

    fun calculateCompOff(
        date: LocalDate,
        shifts: List<ShiftEntity>,
        existing: Map<Int, CompOffEntity>
    ): List<CompOffEntity> {

        val result = mutableListOf<CompOffEntity>()
        val day = date.dayOfWeek

        // ------------------ EARN ------------------
        shifts.filter { it.isHoliday }.forEach {
            val current = existing[it.employeeId]
            result += CompOffEntity(
                employeeId = it.employeeId,
                earned = (current?.earned ?: 0) + 1,
                used = current?.used ?: 0,
                carryForward = current?.carryForward ?: 0
            )
        }

        // ------------------ USE ------------------
        when (day) {
            DayOfWeek.MONDAY -> {
                shifts
                    .filter { it.shiftType == ShiftType.NIGHT.name }
                    .forEach {
                        result += useOneCompOff(it.employeeId, existing)
                    }
            }

            DayOfWeek.WEDNESDAY -> {
                val emp = shifts.first().employeeId
                result += useOneCompOff(emp, existing)
            }

            DayOfWeek.SATURDAY -> {
                shifts.take(2).forEach {
                    result += useOneCompOff(it.employeeId, existing)
                }
            }

            else -> {}
        }

        return result
    }

    private fun useOneCompOff(
        employeeId: Int,
        existing: Map<Int, CompOffEntity>
    ): CompOffEntity {
        val current = existing[employeeId]
        return CompOffEntity(
            employeeId = employeeId,
            earned = current?.earned ?: 0,
            used = (current?.used ?: 0) + 1,
            carryForward = current?.carryForward ?: 0
        )
    }
}