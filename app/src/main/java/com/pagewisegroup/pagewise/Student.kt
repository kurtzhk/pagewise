package com.pagewisegroup.pagewise

import com.pagewisegroup.pagewise.schedule.PlannedDay
import java.io.Serializable
import java.lang.System.currentTimeMillis
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

// Class containing assignments
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null) : Serializable

/**
 * Student with given id and name containing [PWClass], [Assignment], schedule
 */
class Student(var name: String, var id: Long? = null) : Serializable {
    private var classes = ArrayList<PWClass>()
    private var schedule = ArrayList<PlannedDay>()
    private var readingSpeed = ArrayList<Double>()

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
                if(!it.getCompleted())
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
        val byAssignmentSchedule = ArrayList<PlannedDay>()
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
        val byAssign = ArrayList<Double>()
        var index = 0
        classes.forEach {
            it.assignments.forEach {
                if(!it.getCompleted()) byAssign.add(readingSpeed[index])
            }
            index++
        }
        return byAssign
    }

    //Returns pages read per day for the past n days
    fun getReadingHistory(days: Int) : IntArray {
        if(days <= 0) return IntArray(0)
        val arr = IntArray(days)

        classes.forEach{
            it.assignments.forEach{
                val now = currentTimeMillis()
                it.getProgress().getSessions().forEach{
                    val daysAgo = TimeUnit.DAYS.convert(now - it.startTime,TimeUnit.MILLISECONDS)
                    if(daysAgo < days) {
                        if(daysAgo <= 0){
                            return IntArray(0)
                        } else {
                            arr[daysAgo.toInt()] += it.endPage - it.startPage
                        }
                    }
                }
            }
        }
        return arr
    }

    //prints info about student
    override fun toString(): String {
        val studentInfo = StringBuilder()
            .append("name: $name read speed: $readingSpeed ppm id: $id\n")
        classes.forEach {
            studentInfo.append("Class ${it.name} \n\t Assignments: ")
            if(it.assignments.isNotEmpty()) {
                for(i in 0 until (it.assignments.size-1)) {
                    studentInfo.append("${it.assignments[i].name} (Page ${it.assignments[i].getProgress().getCurrentPage()}), ")
                }
                studentInfo.append("${it.assignments[it.assignments.size-1].name} (Page ${it.assignments[it.assignments.size-1].getProgress().getCurrentPage()})\n")
            } else {
                studentInfo.append("N/A \n")
            }
        }
        return studentInfo.toString()
    }

    //getters
    fun getClasses() : ArrayList<PWClass> { return classes }
    fun getSchedule() : ArrayList<PlannedDay> { return schedule }
    fun getReadingSpeed() : ArrayList<Double> { return readingSpeed }

    //setters
    fun setSchedule(days: ArrayList<PlannedDay>) { schedule = days}
}