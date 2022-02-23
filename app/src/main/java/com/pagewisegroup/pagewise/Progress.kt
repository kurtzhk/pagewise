package com.pagewisegroup.pagewise

import android.content.Context
import java.io.Serializable
import java.util.*
import kotlin.Comparator

//represents a user's history with a particular assignment
class Progress(val assignment: Assignment) : Serializable {
    private val sessions: MutableList<ReadingSession> = mutableListOf()

    fun addSessionDB(context: Context, studentId: Long?,endPage: Int, startTime: Long, endTime: Long){
        val startPage = getCurrentPage()
        if(endPage >= assignment.pageEnd) assignment.completed = true
        val rs = ReadingSession(startPage,endPage,startTime,endTime)
        if(sessionExists(rs)) return
        sessions.add(rs)
        //Adds to database
        //This is messy af, but we have no way of access student controller & its database from here
        val dbm = DatabaseManager(context)
        dbm.recordSession(rs, assignment.id!!, studentId!!)
    }

    fun addSession(session: ReadingSession) {
        if(!sessionExists(session)) sessions.add(session)
    }

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
    //possible future thing: support for reading out of order? (this would be a whole lot of work)
    fun getCurrentPage(): Int {
        sessions.sortedWith(compareBy({it.endPage})) //in case somehow out of order sorts
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