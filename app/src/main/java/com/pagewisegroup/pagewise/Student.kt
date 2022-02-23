package com.pagewisegroup.pagewise

import android.util.Log
import java.io.Serializable
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null) : Serializable

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var id: Long? = null) : Serializable {
    val classes = ArrayList<PWClass>()
    var schedule = ArrayList<PlannedDay>()
    //reading speed for classes
    var readingSpeed = ArrayList<Double>()

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

    //gets assignment by name
    fun getAssignment(assignName: String) : Assignment {
        val allAssignments = getAllAssignments()
        allAssignments.forEach {
            if(it.name == assignName) return it
        }
        return allAssignments[0]
    }

    //gets all class names
    fun getAllClassNames() : ArrayList<String> {
        val className = ArrayList<String>()
        classes.forEach {
            className.add(it.name)
        }
        return className
    }

    //gets all assignment names for a given class
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

    //gets reading time by assignment
    fun getReadingSpeedByAssign() : ArrayList<Double> {
        var byAssign = ArrayList<Double>()
        var index = 0
        classes.forEach {
            it.assignments.forEach {
                if(!it.completed) byAssign.add(readingSpeed[index])
            }
            index++
        }
        return byAssign
    }

    //Returns pages read per day for the past n days
    fun getReadingHistory(days: Int) : IntArray {
        val arr = IntArray(days)

        classes.forEach{
            it.assignments.forEach{
                var now = currentTimeMillis()
                it.progress?.getSessions()?.forEach{
                    var daysAgo = TimeUnit.DAYS.convert(now - it.startTime,TimeUnit.MILLISECONDS)
                    if(daysAgo < arr.size){
                        if(daysAgo < 0){
                            Log.w("Student","Encountered reading session from the future")
                        }
                        else{
                            arr[daysAgo.toInt()] += it.endPage - it.startPage
                        }
                    }
                }
            }
        }
        return arr
    }

    //TEMP for testing
    fun printProgress() {
        classes.forEach {
            it.assignments.forEach {
                val assignName = it.name
                it.progress.getSessions().forEach {
                    Log.d("SESSION", "$assignName: ${it.startPage} to ${it.endPage} ")
                }
            }
        }
    }

    //prints info about student
    override fun toString(): String {
        val studentInfo = StringBuilder()
            .append("name: $name read speed: $readingSpeed ppm id: $id\n")
        classes.forEach {
            studentInfo.append("Class ${it.name} \n\t Assignments: ")
            if(!it.assignments.isEmpty()) {
                for(i in 0 until (it.assignments.size-1)) {
                    studentInfo.append("${it.assignments[i].name} (Page ${it.assignments[i].progress.getCurrentPage()}), ")
                }
                studentInfo.append("${it.assignments[it.assignments.size-1].name} (Page ${it.assignments[it.assignments.size-1].progress.getCurrentPage()})\n")
            } else {
                studentInfo.append("N/A \n")
            }
        }
        return studentInfo.toString()
    }
}