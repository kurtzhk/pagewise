package com.pagewisegroup.pagewise

import android.content.Context
import java.io.Serializable

/**
 * Represents reading history of a given assignment
 */
class Progress(val assignment: Assignment) : Serializable {
    private val sessions: MutableList<ReadingSession> = mutableListOf()

    //Adds reading session to database
    fun addSessionDB(context: Context, studentId: Long?,endPage: Int, startTime: Long, endTime: Long){
        val startPage = getCurrentPage()
        if(endPage >= assignment.pageEnd) assignment.completed = true
        val rs = ReadingSession(startPage,endPage,startTime,endTime)
        if(sessionExists(rs)) return
        //Adds to database
        val dbm = DatabaseManager(context)
        dbm.recordSession(rs, assignment.id!!, studentId!!)
        sessions.add(rs)

    }

    //Adds reading session
    fun addSession(session: ReadingSession) {
        if(!sessionExists(session)) sessions.add(session)
    }

    //gets reading session
    fun getSessions() : MutableList<ReadingSession>{
        return sessions
    }

    //checks to see if session already exists
    fun sessionExists(rs: ReadingSession) : Boolean{
        sessions.forEach {
            //there is no situation that you should reread the exact same pages
            if(it.startPage == rs.startPage && it.endPage == rs.endPage) return true
        }
        return false
    }

    //page the user left off on and will continue from
    fun getCurrentPage(): Int {
        sessions.sortedWith(compareBy { it.endPage }) //in case somehow out of order sorts
        if(sessions.isEmpty()) return assignment.pageStart
        if(sessions.last().endPage>assignment.pageEnd) sessions.last().endPage = assignment.pageEnd
        return sessions.last().endPage
    }

    //fraction of assignment complete between 0 and 1
    fun getPortionComplete(): Float {
        return (getCurrentPage() - assignment.pageStart).toFloat()/(assignment.pageEnd - assignment.pageStart)
    }
}

// Single reading session
data class ReadingSession(val startPage: Int, var endPage: Int, val startTime: Long, val endTime: Long):Serializable