package com.pagewisegroup.pagewise

import android.content.Context
import android.util.Log
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null) : Serializable

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var id: Long? = null, val context: Context) : Serializable {
    val classes = ArrayList<PWClass>()
    var schedule = ArrayList<PlannedDay>()
    var pastReadings = ArrayList<ReadingSession>()
    var readingSpeed = 0.0

    //list of all assignments without classes
    fun getAllAssignments() : ArrayList<Assignment> {
        val assignments = ArrayList<Assignment>()
        classes.forEach {
            it.assignments.forEach {
                assignments.add(it)
            }
        }
        return assignments
    }

    fun getAllClassNames() : ArrayList<String> {
        val className = ArrayList<String>()
        classes.forEach {
            className.add(it.name)
        }
        return className
    }

    fun getAssignNames(className : String?) : ArrayList<String> {
        val assignName = ArrayList<String>()
        if(className.isNullOrEmpty()) {
            return assignName
        }
        classes.forEach {
            if(className == it.name) {
                it.assignments.forEach {
                    assignName.add(it.name)
                }
            }
        }
        return assignName
    }

    //finds and returns all unfinished assignments
    fun getUnfishedAssignments(): ArrayList<Assignment> {
        val unfishedAssignments = ArrayList<Assignment>()
        classes.forEach {
            it.assignments.forEach {
                if(!it.completed)
                    unfishedAssignments.add(it)
            }
        }
        return unfishedAssignments
    }

    //Returns the index of a class if it already exists, otherwise returns -1
    fun getClassIndex(name: String) : Int {
        var i = 0
        classes.forEach {
            i++
            if(it.name == name) return i
        }
        return -1
    }

    //Returns the index of a assignment if it already exists, otherwise returns -1
    fun getAssignIndex(name: String, pwClass: PWClass) : Int {
        var i = 0
        pwClass.assignments.forEach {
            i++
            if(it.name == name) return i
        }
        return -1
    }

    //checks if assign exists in given class
    fun assignExists(className: String, assignName: String) : Boolean {
        classes.forEach {
            if(it.name == className) {
                it.assignments.forEach {
                    if(it.name == assignName) { return true}
                }
                return false
            }
        }
        return false
    }

    //Gets schedule where there is one assignment per class
    fun getScheduleByAssignment() : ArrayList<PlannedDay> {
        var byAssignmentSchedule = ArrayList<PlannedDay>()
        var index = 0
        schedule.forEach {
            val size = it.reading.size
            for(i in 0 until size) {
                byAssignmentSchedule.add(PlannedDay(it.date, ArrayList()))
                byAssignmentSchedule[byAssignmentSchedule.size-1].reading.add(it.reading[i])
            }
        }
        return byAssignmentSchedule
    }



    override fun toString(): String {
        val studentInfo = StringBuilder()
            .append("name: $name read speed: $readingSpeed ppm id: $id\n")
        classes.forEach {
            studentInfo.append("Class ${it.name} \n\t Assignments: ")
            if(!it.assignments.isEmpty()) {
                for(i in 0 until (it.assignments.size-1)) {
                    studentInfo.append("${it.assignments[i].name}, ")
                }
                studentInfo.append("${it.assignments[it.assignments.size-1].name}\n")
            } else {
                studentInfo.append("N/A \n")
            }
        }
        return studentInfo.toString()
    }
}