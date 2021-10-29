package com.pagewisegroup.pagewise

import java.util.Date

// Object that tracks an assignment.
class Assignment(val name: String, var dueDate: Date, val pageStart: Int, val pageEnd: Int) {
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