package com.pagewisegroup.pagewise

import android.content.Context
import com.pagewisegroup.pagewise.schedule.SchedulePlanner

/**
 * Manages [Student] object updating and creating [Assignment], [PWClass], and schedule
 */
class StudentController (context: Context, val student: Student) {
    private val dbm: DatabaseManager = DatabaseManager(context)
    private lateinit var schedulePlanner: SchedulePlanner

    init {
        if(student.getClasses().isEmpty()) fetchClasses() //avoid class duplication
        calculateReadingSpeeds()
    }

    //gets all classes from db and adds them to class arraylist
    private fun fetchClasses() {
        val db = dbm.writableDatabase
        for(i in 1..dbm.numberOfClasses(db)) {
            student.getClasses().add(dbm.fetchClass(i.toLong()))
        }
    }

    //adds class to student && database
    fun addClass(name: String) {
        if(student.getClassIndex(name) > 0) return
        val pwClass = PWClass(name,ArrayList(),null)
        student.getClasses().add(pwClass)
        dbm.recordClass(pwClass)
    }

    //adds assignment to student && database
    fun addAssignment(assignment: Assignment, className: String) {
        val pwClass = student.getClasses()[student.getClassIndex(className)-1]
        if(student.getAssignIndex(assignment.name,pwClass) > 0) return
        pwClass.assignments.add(assignment)
        dbm.recordAssignment(assignment,student.getClassIndex(className).toLong())
    }

    //adds reading assignment to student && database
    fun addReadingSession(assignName: String, startPage: Int, endPage: Int, startTime: Long, endTime: Long) {
        val rs = ReadingSession(startPage,endPage,startTime,endTime)
        if(student.getAssignment(assignName).getProgress().sessionExists(rs)) return
        student.getAssignment(assignName).getProgress().addSession(rs)
        dbm.recordSession(rs, student.getAssignment(assignName).getId()!!, student.id!!)
    }

    /* creates assignment from uniqueString */
    //Not implemented
    fun createAssignmentUniqueString(uniqueString: String, className: String) {
        if(uniqueString.isBlank()) return
        val assignment = Assignment.fromUniqueString(uniqueString)
        addAssignment(assignment,className)
    }

    //calculates reading speeds for all classes
    fun calculateReadingSpeeds() {
        //finds total readings speed
        var totalReadingSpeed = calculateReadingSpeed(null)
        //If there is no readings record do the average of a page a minute
        if(totalReadingSpeed == 0.0) totalReadingSpeed = 1.0
        student.getClasses().forEach {
            val classReadingSpeed = calculateReadingSpeed(it.name)
            //if there is > 5 sessions adds classReading speed, else adds total
            if(classReadingSpeed == 0.0) {
                student.getReadingSpeed().add(totalReadingSpeed)
                it.assignments.forEach { it.updateCompletionEstimate(totalReadingSpeed) }
            } else {
                student.getReadingSpeed().add(classReadingSpeed)
                it.assignments.forEach { it.updateCompletionEstimate(classReadingSpeed) }
            }
        }
    }

    //calculates reading speed for specific class if given else the global total
    fun calculateReadingSpeed(className: String?) : Double {
        var total = 0.0
        var size = 0
        student.getClasses().forEach {
            val currentClass = it.name
            it.assignments.forEach {
                if((className != null && currentClass == className) || className == null) {
                    it.getProgress().getSessions().forEach {
                        val time = (it.endTime-it.startTime).toDouble()/60000
                        //only includes reading sessions longer then 1 minute
                        if(time > 1.0) {
                            total += (it.endPage - it.startPage) / time
                            size++
                        }
                    }
                }
            }
        }
        //only calculates by class if it has > 5 sessions
        return if(size == 0 || (!className.isNullOrEmpty() && size < 5)) 0.0
        else total/size
    }

    //creates schedule for student
    fun createSchedule() {
        //calculates reading speeds
        calculateReadingSpeeds()
        //creates schedule
        schedulePlanner = SchedulePlanner(student.getUnfishedAssignments(),student.getReadingSpeedByAssign())
        //sets student schedule
        student.setSchedule(schedulePlanner.getSchedule())
    }
}