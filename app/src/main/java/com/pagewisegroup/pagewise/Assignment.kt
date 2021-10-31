package com.pagewisegroup.pagewise

import java.util.Date

// Object that tracks an assignment.
// id field should only be populated when reading from or writing to database.
class Assignment(var id: Long? = null, val name: String, var dueDate: Date, var pageStart: Int, var pageEnd: Int) {
    var completed: Boolean = false
    var hoursToComplete: Double = 0.0

    fun updateCompletionEstimate(pagesPerHour: Double) {
        hoursToComplete = (pageEnd - pageStart) / pagesPerHour
    }

    fun completeAssignment(hoursToComplete: Double) {
        this.hoursToComplete = hoursToComplete
        completed = true
    }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append(name)
            .append("\n")
            .append(pageStart)
            .append("-")
            .append(pageEnd)
            .append("\n")
            .append(dueDate.toString())
        return builder.toString()
    }
}