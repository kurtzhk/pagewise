package com.pagewisegroup.pagewise

import java.util.Date

class Assignment(val name: String, var dueDate: Date, val pageStart: Int, val pageEnd: Int) {
    var estimatedHours: Double = 0.0

    fun updateCompletionEstimate(pagesPerHour: Double) {
        estimatedHours = (pageEnd - pageStart) / pagesPerHour
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