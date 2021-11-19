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

    //calculates reading speed in pages per minute based on class if given, if class is not given bases it on all assignments
    fun calculateReadingSpeed(className: String?) {
        // TODO("Update this to work with reading sessions/activities is integrated into assignment/student")
        var total = 0.0
        var size = 0
        classes.forEach {
            val currentClass = it.name
            it.assignments.forEach {
                //TODO("Update once reading session finished to work with all sessions not just completed assignments")
                if((className != null && currentClass == className) || className == null) {
                    if (it.completed) {
                        total += (it.pageEnd - it.pageStart) / it.minutesSpend
                        size++
                    }
                }
            }
        }
        //calculates mean
        if (size != 0)
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
        classes[0].assignments[0].minutesSpend = 75.0
        classes[0].assignments[0].completed = true
        classes[0].assignments.add(Assignment("Assignment 2", Date(),50,100))
        classes[0].assignments[1].minutesSpend = 100.0
        classes[0].assignments[1].completed = true

        //unfinished assignments
        classes[0].assignments.add(Assignment("Assignment 3", Date(121,10,28,23,59),0,45))
        classes[0].assignments.add(Assignment("Assignment 4", Date(121,10,29,23,59),0,75))
        classes[0].assignments.get(3).currentPage = 25
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
            it.assignments.forEach {
                Log.d("Class $classname has assignment", it.name)
            }
        }
    }

    override fun toString(): String {
        return "name: $name read speed: $reading_speed ppm id: $id"
    }
}