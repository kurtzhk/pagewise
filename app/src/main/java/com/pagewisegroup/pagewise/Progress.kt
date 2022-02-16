package com.pagewisegroup.pagewise

import java.io.Serializable

//represents a user's history with a particular assignment
class Progress(val assignment: Assignment) : Serializable {
    private val sessions: MutableList<ReadingSession> = mutableListOf()

    fun addSession(endPage: Int, startTime: Long, endTime: Long){
        var startPage = getCurrentPage()
        if(sessions.isNotEmpty()) startPage  = sessions.last().endPage
        if(endPage >= assignment.pageEnd) assignment.completed = true
        var rs = ReadingSession(startPage,endPage,startTime,endTime)
        sessions.add(rs)
    }

    fun getSessions() : MutableList<ReadingSession>{
        return sessions
    }

    //page the user left off on and will continue from
    //possible future thing: support for reading out of order? (this would be a whole lot of work)
    fun getCurrentPage(): Int {
        if(sessions.isEmpty()) return assignment.pageStart
        return sessions.last().endPage
    }

    //fraction of assignment complete between 0 and 1
    //note: would need a separate check for completely finished
    fun getPortionComplete(): Float {
        return (getCurrentPage() - assignment.pageStart).toFloat()/(assignment.pageEnd - assignment.pageStart + 1)
    }
}

data class ReadingSession(val startPage: Int, val endPage: Int, val startTime: Long, val endTime: Long):Serializable