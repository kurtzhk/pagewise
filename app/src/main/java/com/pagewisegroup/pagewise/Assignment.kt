package com.pagewisegroup.pagewise

import java.nio.charset.StandardCharsets
import java.util.*

class Assignment(val name: String, var dueDate: Date, val pageStart: Int, val pageEnd: Int) {
    var estimatedHours: Double = 0.0
    var assignID: String = ""

    init {
        if(assignID.isBlank()) createID()
    }

    /* Builder object, so can create assignment from id */
    companion object IDBuilder {
        fun fromId(id: String): Assignment {
            val codedAssignInfo = id.split("&")
            if(codedAssignInfo.size != 6) error("Incorrect assignment ID string")
            val date = Date(base64Decode(codedAssignInfo[1]).toInt(), base64Decode(codedAssignInfo[2]).toInt(),base64Decode(codedAssignInfo[3]).toInt())
            return Assignment(base64Decode(codedAssignInfo[0]), date, base64Decode(codedAssignInfo[4]).toInt(),base64Decode(codedAssignInfo[5]).toInt())
        }
        /* Decodes given str to base 64  */
        fun base64Decode(str: String) : String { return String(android.util.Base64.decode(str, android.util.Base64.DEFAULT), StandardCharsets.UTF_8).trim() }
    }

    /* Creates unique string id for assignment */
    fun createID() {
        if(name.isBlank()) return
        assignID = base64Encode(name) + "&" + base64Encode((dueDate.year).toString()) + "&" +
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
            .append(assignID)
        return builder.toString()
    }

    fun updateCompletionEstimate(pagesPerHour: Double) {
        estimatedHours = (pageEnd - pageStart) / pagesPerHour
    }
}