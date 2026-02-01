package com.pktech.newapp.logic

import com.pktech.newapp.data.local.entity.EmployeeEntity
import com.pktech.newapp.data.local.entity.ShiftEntity
import java.time.DayOfWeek
import java.time.LocalDate

class ShiftScheduler(
    private val employees: List<EmployeeEntity>,
    private val fixedMorningEmployeeId: Int,
    private val rotationState: RotationState
) {

    fun generateDaySchedule(date: LocalDate, isHoliday: Boolean): List<ShiftEntity> {
        val result = mutableListOf<ShiftEntity>()
        val day = date.dayOfWeek

        return if (isHoliday) {
            generateHolidaySchedule(date)
        } else {
            when (day) {
                DayOfWeek.MONDAY -> mondaySchedule(date)
                DayOfWeek.TUESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY -> fullWorkingDaySchedule(date)
                DayOfWeek.WEDNESDAY -> wednesdaySchedule(date)
                DayOfWeek.SATURDAY -> saturdaySchedule(date)
                else -> emptyList()
            }
        }
    }

    // -------------------- WORKING DAYS --------------------

    private fun mondaySchedule(date: LocalDate): List<ShiftEntity> {
        val workingEmployees = employees.shuffled().take(5)
        return assignStandardShifts(date, workingEmployees)
    }

    private fun fullWorkingDaySchedule(date: LocalDate): List<ShiftEntity> {
        val result = mutableListOf<ShiftEntity>()

        // Morning (fixed)
        result += ShiftEntity(
            employeeId = fixedMorningEmployeeId,
            date = date,
            shiftType = ShiftType.MORNING.name,
            isHoliday = false,
            isCompOffUsed = false
        )

        // Night (rotating)
        val nightEmployee = rotatingNightEmployee()
        result += ShiftEntity(
            employeeId = nightEmployee.id,
            date = date,
            shiftType = ShiftType.NIGHT.name,
            isHoliday = false,
            isCompOffUsed = false
        )

        // Remaining shifts
        val others = employees
            .filter { it.id != fixedMorningEmployeeId && it.id != nightEmployee.id }
            .shuffled()

        result += ShiftEntity(0, others[0].id, date, ShiftType.GENERAL.name, false, false)
        result += ShiftEntity(0, others[1].id, date, ShiftType.GENERAL.name, false, false)
        result += ShiftEntity(0, others[2].id, date, ShiftType.MID.name, false, false)
        result += ShiftEntity(0, others[3].id, date, ShiftType.SECOND.name, false, false)

        return result
    }

    private fun wednesdaySchedule(date: LocalDate): List<ShiftEntity> {
        val workingEmployees = employees.shuffled().take(5)
        return assignStandardShifts(date, workingEmployees)
    }

    private fun saturdaySchedule(date: LocalDate): List<ShiftEntity> {
        val weekOfMonth = (date.dayOfMonth - 1) / 7 + 1
        return if (weekOfMonth == 2 || weekOfMonth == 4) {
            generateHolidaySchedule(date)
        } else {
            val workingEmployees = employees.shuffled().take(4)
            assignStandardShifts(date, workingEmployees)
        }
    }

    // -------------------- HOLIDAY --------------------

    private fun generateHolidaySchedule(date: LocalDate): List<ShiftEntity> {
        val dayEmployee = rotatingHolidayEmployee()
        val nightEmployee = rotatingHolidayEmployee()

        return listOf(
            ShiftEntity(0, dayEmployee.id, date, ShiftType.DAY_HOLIDAY.name, true, false),
            ShiftEntity(0, nightEmployee.id, date, ShiftType.NIGHT_HOLIDAY.name, true, false)
        )
    }

    // -------------------- HELPERS --------------------

    private fun assignStandardShifts(
        date: LocalDate,
        employees: List<EmployeeEntity>
    ): List<ShiftEntity> {
        val shifts = listOf(
            ShiftType.MORNING,
            ShiftType.GENERAL,
            ShiftType.GENERAL,
            ShiftType.SECOND,
            ShiftType.NIGHT
        )

        return employees.mapIndexed { index, emp ->
            ShiftEntity(
                employeeId = emp.id,
                date = date,
                shiftType = shifts[index].name,
                isHoliday = false,
                isCompOffUsed = false
            )
        }
    }

    private fun rotatingNightEmployee(): EmployeeEntity {
        val emp = employees[rotationState.nightShiftIndex % employees.size]
        rotationState.nightShiftIndex++
        return emp
    }

    private fun rotatingHolidayEmployee(): EmployeeEntity {
        val emp = employees[rotationState.compOffIndex % employees.size]
        rotationState.compOffIndex++
        return emp
    }
}