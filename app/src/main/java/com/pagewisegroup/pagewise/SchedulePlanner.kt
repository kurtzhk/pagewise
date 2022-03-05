package com.pagewisegroup.pagewise

import android.icu.text.DateFormatSymbols
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.floor

data class PlannedReading(var assignmentName: String, var plannedMinutes: Double, var startPages: Int, var endPage: Int) : Serializable
data class PlannedDay(val date: Date, var reading: ArrayList<PlannedReading>): Serializable

//Right now this could be merged with student, but when updated in probably will be nice to have it independent
//TODO("Overhaul once have more data to work with")
class SchedulePlanner (unfinishedAssignments: ArrayList<Assignment>, val readingSpeed: ArrayList<Double>) {
    var schedule = ArrayList<PlannedDay>()

    init {
        for(i in 0 until unfinishedAssignments.size)
            updateSchedule(unfinishedAssignments[i],readingSpeed[i])
    }

    //Adds given assignment to schedule
    //right now just evenly split works every day for each assignment
    fun updateSchedule(assignment: Assignment, readingSpeedAssign: Double) {
        val timeManager = TimeManager()
        var currentDate = Date()
        //today only counts today as a work day if there is 10 hr+ left in it
        if(currentDate.hours >= 13) {
            currentDate = timeManager.incrementDay(currentDate)
        }

        //if the assignment is passed due ignore it
        var daysLeft = timeManager.getDateDiff(currentDate,assignment.dueDate,TimeUnit.DAYS)
        if(daysLeft <= 0) return

        val pagesPerDay = (assignment.pageEnd - assignment.progress.getCurrentPage()) / daysLeft.toDouble()

        //Adds day to schedule
        var today = false
        var date = currentDate
        var pageEnd = assignment.progress.getCurrentPage()
        for(i in 1..daysLeft) {
            val pageStart = pageEnd

            //round up except for last day, when you finish
            pageEnd = pageStart + ceil(pagesPerDay).toInt()
            if(pageEnd > assignment.pageEnd)  pageEnd = assignment.pageEnd

            //increments day if it is not today
            if(today) date = timeManager.incrementDay(date)
            else today = true

            //adds/updates day in schedule
            val dayIndex = timeManager.findDayIndex(schedule, date)
            if(dayIndex < 0)
                schedule.add(PlannedDay(date, ArrayList()))
            if(pageEnd-pageStart != 0)
                schedule[timeManager.findDayIndex(schedule, date)].reading.add(PlannedReading(assignment.name,(pageEnd-pageStart)/readingSpeedAssign,pageStart,pageEnd))
        }
    }

    //prints schedule
    @RequiresApi(Build.VERSION_CODES.N)
    override fun toString(): String {
        var scheduleString = ""
        schedule.forEach {
            scheduleString += "On ${DateFormatSymbols().months[it.date.month]} ${it.date.date} \n"
            it.reading.forEach {
                scheduleString += "\t read assignment ${it.assignmentName} for "
                //if greater the an hour converts to hours & minutes
                scheduleString += if(it.plannedMinutes > 60)
                    "${floor(it.plannedMinutes/60)} hours and ${ceil(it.plannedMinutes%60)} minutes"
                else
                    "${floor(it.plannedMinutes)} minutes"
                scheduleString += " from pages ${it.startPages} to ${it.endPage}\n"
            }
        }
        return scheduleString
    }
}