package com.pagewisegroup.pagewise

//represents a user's history with a particular assignment
class Progress(val assignment: Assignment) {
    private val sessions: MutableList<ReadingSession> = mutableListOf()

    fun addSession(session: ReadingSession){
        sessions.add(session)
    }

    //page the user left off on and will continue from
    //possible future thing: support for reading out of order? (this would be a whole lot of work)
    fun getCurrentPage(): Int{
        if(sessions.isEmpty()) return assignment.pageStart
        return sessions.last().endPage
    }

    //fraction of assignment complete between 0 and 1
    //note: would need a separate check for completely finished
    fun getPortionComplete(): Float{
        return (getCurrentPage() - assignment.pageStart).toFloat()/(assignment.pageEnd - assignment.pageStart + 1)
    }
}