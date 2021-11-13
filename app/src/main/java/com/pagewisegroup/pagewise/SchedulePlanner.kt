package com.pagewisegroup.pagewise

import android.icu.text.DateFormatSymbols
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

data class plannedReading(var assignmentName: String, var plannedMinutes: Double, var startPages: Int, var endPage: Int)
data class plannedDay(val date: Date, var reading: ArrayList<plannedReading>)

//Right now this could be merged with student, but when updated in probably will be nice to have it independent
//TODO("Overhaul once have more data to work with")
class SchedulePlanner (val unfishedAssignments: ArrayList<Assignment>, val readingSpeed: Double) {
    var schedule = ArrayList<plannedDay>()

    init {
        unfishedAssignments.forEach {
            updateSchedule(it)
        }
    }

    //Adds given assignment to schedule
    //right now just evenly split works every day for each assignment
    //TODO("Update so this minimizes minutes per day instead of dividing work evanly (or add a choice between the two)")
    fun updateSchedule(assignment: Assignment) {
        var currentDate = Date()
        //today only counts today as a work day if there is 10 hr+ left in it
        if(currentDate.hours >= 13) {
            currentDate = incrementDay(currentDate)
        }

        val daysLeft = getDateDiff(currentDate,assignment.dueDate,TimeUnit.DAYS)

        val pagesPerDay = (assignment.pageEnd - assignment.currentPage) / daysLeft.toDouble()

        //Adds day to schedule
        var today = false
        var date = currentDate
        var pageEnd = assignment.currentPage
        for(i in 1..daysLeft) {
            var pageStart = pageEnd

            //round up except for last day, when you finish
            pageEnd = pageStart + ceil(pagesPerDay).toInt()
            if(pageEnd > assignment.pageEnd)  pageEnd = assignment.pageEnd

            //increments day if it is not today
            if(today) date = incrementDay(date)
            else today = true

            //adds/updates day in schedule
            val dayIndex = findDayIndex(date)
            if(dayIndex < 0)
                schedule.add(plannedDay(date, ArrayList()))
            schedule.get(findDayIndex(date)).reading.add(plannedReading(assignment.name,(pageEnd-pageStart)/readingSpeed,pageStart,pageEnd))
        }
    }

    //returns index of date in schedule
    //returns -1 if not in schedule
    fun findDayIndex(date: Date) : Int {
        for ((index, day) in schedule.withIndex())
            if(day.date.date == date.date && day.date.month == date.month) return index
        return -1
    }

    //calculates the difference in time between two given dates
    //in a given time unit
    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val diffInMillies = date2.time - date1.time
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun incrementDay(date: Date) : Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        return calendar.time
    }

    //prints schedule
    @RequiresApi(Build.VERSION_CODES.N)
    override fun toString(): String {
        var scheduleString = ""
        schedule.forEach {
            scheduleString += "On ${DateFormatSymbols().getMonths()[it.date.month]} ${it.date.date} \n"
            it.reading.forEach {
                scheduleString += "\t read asssignment ${it.assignmentName} for "
                //if greater the an hour converts to hours & minutes
                if(it.plannedMinutes > 60)
                    scheduleString += "${floor(it.plannedMinutes/60)} hours and ${ceil(it.plannedMinutes%60)} minutes"
                else
                    scheduleString += "${floor(it.plannedMinutes)} minutes"
                scheduleString += " from pages ${it.startPages} to ${it.endPage}\n"
            }
        }
        return scheduleString
    }
}