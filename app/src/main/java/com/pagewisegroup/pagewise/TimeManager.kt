package com.pagewisegroup.pagewise

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.abs

//helps manage time with dates and plannedDays
class TimeManager {
    //returns index of date in schedule
    //returns -1 if not in schedule
    fun findDayIndex(currSchedule: ArrayList<PlannedDay>, date: Date) : Int {
        for ((index, day) in currSchedule.withIndex())
            if(day.date.date == date.date && day.date.month == date.month) return index
        return -1
    }

    //calculates the difference in time between two given dates
    //in a given time unit
    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val diffInMillies = date2.time - date1.time
        if(diffInMillies < 0) return -1
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    //increases the day by one
    fun incrementDay(date: Date) : Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        return calendar.time
    }

}