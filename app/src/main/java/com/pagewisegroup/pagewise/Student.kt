package com.pagewisegroup.pagewise

import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null)

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var reading_speed: Double, var id: Long? = null) {
    val classes = ArrayList<PWClass>()

    //calculates reading speed in pages per minute
    fun calculateReadingSpeed() {
        // TODO("Update this to work with reading sessions/activities is integrated into assignment/student")
        var total = 0.0
        var size = 0
        classes.forEach {
            it.assignments.forEach {
                if(it.completed) {
                    total += (it.pageEnd - it.pageStart / it.hoursToComplete/60)
                    size++
                }
            }
        }
        //calculates mean
        reading_speed = total/size
    }

    fun unfishedAssignments(): ArrayList<Assignment> {
        var unfishedAssignments = ArrayList<Assignment>()
        classes.forEach {
            it.assignments.forEach {
                if(!it.completed)
                    unfishedAssignments.add(it)
            }
        }
        return unfishedAssignments
    }

    override fun toString(): String {
        return "name: $name read speed: $reading_speed id: $id"
    }
}