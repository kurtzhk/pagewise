package com.pagewisegroup.pagewise

import android.util.Log
import java.nio.charset.StandardCharsets
import java.util.Date

class Assignment(val name: String, var dueDate: Date, val pageStart: Int, val pageEnd: Int) {
    var completed: Boolean = false
    var hoursToComplete: Double = 0.0
    var id: Long? = null
    var uniqueString = ""

    /* creates unique string on creation */
    init {
        if(uniqueString.isBlank()) createUniqueString()
    }

    fun updateCompletionEstimate(pagesPerHour: Double) {
        hoursToComplete = (pageEnd - pageStart) / pagesPerHour
    }

    fun completeAssignment(hoursToComplete: Double) {
        this.hoursToComplete = hoursToComplete
        completed = true
    }

    /* Builder object, so can create assignment from id */
    companion object uniqueStringBuilder {
        fun fromUniqueString(uniqueString: String): Assignment {
            val codedAssignInfo = uniqueString.split("&")
            if(codedAssignInfo.size != 6) error("Incorrect assignment unique string")
            val date = Date(base64Decode(codedAssignInfo[1]).toInt(), base64Decode(codedAssignInfo[2]).toInt(),base64Decode(codedAssignInfo[3]).toInt())
            return Assignment(base64Decode(codedAssignInfo[0]), date, base64Decode(codedAssignInfo[4]).toInt(),base64Decode(codedAssignInfo[5]).toInt())
        }
        /* Decodes given str to base 64  */
        fun base64Decode(str: String) : String { return String(android.util.Base64.decode(str, android.util.Base64.DEFAULT), StandardCharsets.UTF_8).trim() }
    }

    /* Creates unique string unique string for assignment */
    fun createUniqueString() {
        if(name.isBlank()) return
        /* String is base64 encoded with "&" as separators */
        uniqueString = base64Encode(name) + "&" + base64Encode((dueDate.year).toString()) + "&" +
            base64Encode((dueDate.month).toString()) + "&" + base64Encode((dueDate.date).toString()) + "&" +
            base64Encode(pageStart.toString()) + "&" + base64Encode(pageEnd.toString())
    }

    /* Encodes given str to base 64  */
    fun base64Encode(str: String) : String { return String(android.util.Base64.encode(str.toByteArray(), android.util.Base64.DEFAULT), StandardCharsets.UTF_8).trim() }

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        builder.append(name)
            .append("\n")
            .append(pageStart)
            .append("-")
            .append(pageEnd)
            .append("\n")
            .append(dueDate.toString())
            .append("\n")
            .append(uniqueString)
        return builder.toString()
    }
}