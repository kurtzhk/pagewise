package com.pagewisegroup.pagewise

import java.io.Serializable
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * Information about a reading assignment
 */

class Assignment(val name: String, var dueDate: Date, val pageStart: Int, val pageEnd: Int): Serializable {
    private var completed = false
    private var timeToComplete: Double = 0.0
    private var id: Long? = null
    private var uniqueString = ""
    private var progress: Progress = Progress(this)

    //creates unique string on creation
    init {
        if(uniqueString.isBlank()) createUniqueString()
    }

    //updates current page and time to complete
    fun updateCompletionEstimate(pagesPerMinute: Double) {
        timeToComplete = if(pagesPerMinute <= 0.0) 0.0
        else (pageEnd - progress.getCurrentPage()) / pagesPerMinute
    }

    /* Builder object, so can create assignment from id */
    // This was never implemented into the end product
    companion object UniqueStringBuilder {
        fun fromUniqueString(uniqueString: String): Assignment {
            val codedAssignInfo = uniqueString.split("&")
            if(codedAssignInfo.size != 6) error("Incorrect assignment unique string")
            val date = Date(base64Decode(codedAssignInfo[1]).toInt(), base64Decode(codedAssignInfo[2]).toInt(),base64Decode(codedAssignInfo[3]).toInt())
            return Assignment(base64Decode(codedAssignInfo[0]), date, base64Decode(codedAssignInfo[4]).toInt(),base64Decode(codedAssignInfo[5]).toInt())
        }
        /* Decodes given str to base 64  */
        private fun base64Decode(str: String) : String { return String(android.util.Base64.decode(str, android.util.Base64.DEFAULT), StandardCharsets.UTF_8).trim() }
    }

    /* Creates unique string unique string for assignment */
    private fun createUniqueString() {
        if(name.isBlank()) return
        /* String is base64 encoded with "&" as separators */
        uniqueString = base64Encode(name) + "&" + base64Encode((dueDate.year).toString()) + "&" +
            base64Encode((dueDate.month).toString()) + "&" + base64Encode((dueDate.date).toString()) + "&" +
            base64Encode(pageStart.toString()) + "&" + base64Encode(pageEnd.toString())
    }

    //Encodes given str to base 64
    private fun base64Encode(str: String) : String { return String(android.util.Base64.encode(str.toByteArray(), android.util.Base64.DEFAULT), StandardCharsets.UTF_8).trim() }

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

    //getters
    fun getProgress() : Progress { return progress }
    fun getCompleted() : Boolean { return completed }
    fun getTimeToComplete() : Double { return timeToComplete}
    fun getId() : Long? { return id }

    //setters
    fun setProgress(prog: Progress) {progress = prog}
    fun setCompleted(complete : Boolean) { completed = complete}
    fun setTimeToComplete(time : Double) { timeToComplete = time}
    fun setId(id: Long?) {this.id = id}
}