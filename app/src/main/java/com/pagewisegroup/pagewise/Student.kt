package com.pagewisegroup.pagewise

import android.util.Log
import java.lang.Error
import java.util.*
import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null)

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var reading_speed: Double, var id: Long? = null) {
    val classes = ArrayList<PWClass>()
    var schedule: SchedulePlanner? = null

    //calculates reading speed in pages per minute
    fun calculateReadingSpeed() {
        // TODO("Update this to work with reading sessions/activities is integrated into assignment/student")
        var total = 0.0
        var size = 0
        classes.forEach {
            it.assignments.forEach {
                //TODO("Update once reading session finished to work with all sessions not just completed assignments")
                if(it.completed) {
                    total += (it.pageEnd - it.pageStart) / it.minutesSpend
                    size++
                }
            }
        }
        //calculates mean
        reading_speed = total/size
    }

    //finds and returns all unfinished assignments
    fun unfishedAssignments(): ArrayList<Assignment> {
        val unfishedAssignments = ArrayList<Assignment>()
        classes.forEach {
            it.assignments.forEach {
                if(!it.completed)
                    unfishedAssignments.add(it)
            }
        }
        return unfishedAssignments
    }

    //temp for testing/demoing scheduling
    fun createTempAssignments() {
        //creates class
        classes.add(PWClass("Test Class", ArrayList() ,null))
        //finished assignments (to get reading speed)
        classes[0].assignments.add(Assignment("Assignment 1", Date(),0,25))
        classes[0].assignments[0].minutesSpend = 25.0
        classes[0].assignments[0].completed = true
        classes[0].assignments.add(Assignment("Assignment 2", Date(),50,100))
        classes[0].assignments[1].minutesSpend = 50.0
        classes[0].assignments[1].completed = true

        var date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DATE, 1)
        date = calendar.time

        //unfinished assignments
        classes[0].assignments.add(Assignment("Assignment 3", Date(121,10,15,23,59),0,20))
        //Log.d("Unfinished assignment #1",classes[0].assignments[2].toString())
        //classes[0].assignments[2].currentPage = 0

        classes[0].assignments.add(Assignment("Assignment 4", Date(121,10,16,23,59),0,10))
        //Log.d("Unfinished assignment #2",classes[0].assignments[3].toString())

    }

    fun createSchedule() {
        if(reading_speed == 0.0)
            error("Please complete assignment, so we can calculate reading speed")
        if(unfishedAssignments().isEmpty())
            error("Please enter an assignment")
        schedule = SchedulePlanner(unfishedAssignments(),reading_speed)
    }

    fun printClasses() {
        classes.forEach {
            val classname = it.name
            Log.d("$classname has class", it.name)
            it.assignments.forEach {
                Log.d("Class $classname has assignment", it.name)
            }
        }
    }

    override fun toString(): String {
        return "name: $name read speed: $reading_speed id: $id"
    }
}