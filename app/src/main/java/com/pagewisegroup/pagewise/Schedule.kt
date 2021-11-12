package com.pagewisegroup.pagewise

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

data class plannedDay(val date: Date, var reading: ArrayList<Pair<String,Double>>)

//Right now this could be merged with student, but when updated in probably will be nice to have it independent
//TODO("Overhaul once have more data to work with")
class Schedule (val unfishedAssignment: ArrayList<Assignment>, val readingSpeed: Double) {
    var schedule = ArrayList<plannedDay>()

    init {
        unfishedAssignment.forEach {
            updateSchedule(it)
        }
    }

    //Adds given assignment to schedule
    fun updateSchedule(assignment: Assignment) {
        val currentDate = Date()
        val daysLeft = getDateDiff(assignment.dueDate,currentDate,TimeUnit.DAYS)
        //TODO("update with current page once that is implemented")
        val minutesPerDay = ((assignment.pageEnd - assignment.pageStart)/ daysLeft) / readingSpeed

        //Adds day to schedule
        for(i in 0..daysLeft) {
            //increments day
            var date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 1)
            date = calendar.time

            //adds/updates day in schedule
            val dayIndex = findDayIndex(date)
            if(dayIndex < 0)
                schedule.add(plannedDay(date, ArrayList()))
            schedule.get(dayIndex).reading.add(Pair(assignment.name,minutesPerDay))
        }
    }

    //returns index of date in schedule
    //returns -1 if not in schedule
    fun findDayIndex(date: Date) : Int {
        for ((index, day) in schedule.withIndex()) {
            if(day.date.equals(date))
                return index
        }
        return -1
    }

    //calculates the diffrence in time between two given dates
    //in a given time unit
    fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
        val diffInMillies = date2.time - date1.time
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }
}