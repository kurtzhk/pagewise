package com.pagewisegroup.pagewise

import android.content.Context
import android.util.Log
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null) : Serializable

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var id: Long? = null) : Serializable {
    val classes = ArrayList<PWClass>()
    var readingSpeed = 0.0
    var schedule = ArrayList<PlannedDay>()

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