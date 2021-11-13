package com.pagewisegroup.pagewise

import android.icu.text.DateFormatSymbols
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.floor
import kotlin.math.round

data class plannedDay(val date: Date, var reading: ArrayList<Pair<String,Double>>)

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
    fun updateSchedule(assignment: Assignment) {
        var currentDate = Date()
        //today only counts today as a work day if there is 10 hr+ left in it
        if(currentDate.hours >= 13) {
            Log.d("Remove", "moving to next day")
            currentDate = incrementDay(currentDate)
        }

        val daysLeft = getDateDiff(currentDate,assignment.dueDate,TimeUnit.DAYS)
        Log.d("Remove", "The diffrence between ${assignment.dueDate} and $currentDate is $daysLeft")
        //TODO("update with current page once that is implemented")
        val minutesPerDay = ((assignment.pageEnd - assignment.pageStart)/ daysLeft) / readingSpeed

        //Adds day to schedule
        var today = false
        var date = currentDate
        for(i in 1..daysLeft) {
            //increments day if it is not today
            if(today) date = incrementDay(date)
            else today = true

            //adds/updates day in schedule
            val dayIndex = findDayIndex(date)
            if(dayIndex < 0)
                schedule.add(plannedDay(date, ArrayList()))
            schedule.get(findDayIndex(date)).reading.add(Pair(assignment.name,minutesPerDay))
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
        Log.d("Remove", ""+schedule.size)
        var scheduleString = ""
        schedule.forEach {
            scheduleString += "On ${DateFormatSymbols().getMonths()[it.date.month]} ${it.date.date} \n"
            it.reading.forEach {
                scheduleString += "\t read asssignment ${it.first} for "
                //if greater the an hour converts to hours & minutes
                if(it.second > 60)
                    scheduleString += "${floor(it.second/60)} hours and ${round(it.second%60)} minutes"
                else
                    scheduleString += "${it.second} minutes"
                scheduleString += "\n"
            }
        }
        return scheduleString
    }
}