package com.pagewisegroup.pagewise

import android.content.Context
import android.util.Log
import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.collections.ArrayList

class StudentController (context: Context, val student: Student) {
    private val dbm: DatabaseManager = DatabaseManager(context)
    private val schedulePlanner: SchedulePlanner

    init {
        if(student.classes.isEmpty()) fetchClasses() //avoid class duplication
        calculateReadingSpeed(null)

        //temp values replace once reading sessions are in database
        student.readingSpeed = .5
        createTempAssignments()
        createTempProgress() //for testing charts

        schedulePlanner = SchedulePlanner(student.getUnfishedAssignments(),student.readingSpeed)
        createSchedule()
    }

    //gets all classes from db and adds them to class arraylist
    fun fetchClasses() {
        val db = dbm.writableDatabase
        for(i in 1..dbm.numberOfClasses(db)) {
            student.classes.add(dbm.fetchClass(i.toLong()))
        }
    }

    //adds class to student && database
    fun addClass(name: String) {
        if(student.getClassIndex(name) > 0) { return }
        val pwClass = PWClass(name,ArrayList(),null)
        student.classes.add(pwClass)
        dbm.recordClass(pwClass)
    }

    //adds assignment to student && database
    fun addAssignment(assignment: Assignment, className: String) {
        val pwClass = student.classes[student.getClassIndex(className)-1]
        if(student.getAssignIndex(assignment.name,pwClass) > 0) { return }
        pwClass.assignments.add(assignment)
        dbm.recordAssignment(assignment,student.getClassIndex(className).toLong())
    }

    //adds reading assignment to student && database
    fun addReadingSession(assignName: String, startPage: Int, endPage: Int, startTime: Long, endTime: Long) {
        Log.d("Reading session", "$assignName from $startPage - $endPage over ${(endTime-startTime)/60000} mins")
        student.getAssignment(assignName).progress.addSession(endPage, startTime,endTime)
        //TODO Add to database once incorporated
    }

    //temp for testing/demoing scheduling
    fun createTempAssignments() {
        //creates class
        addClass("CSCI 492")
        addClass("English 101")
        addClass("Bio 101")
        addAssignment(Assignment("Some CSCI Paper", Date(2022-1900,0,26,23,59,59),0,1000),"CSCI 492")
        addAssignment(Assignment("Kotlin Research", Date(2022-1900,0,28,23,59,59),5,53),"CSCI 492")
        addAssignment(Assignment("English Book #5", Date(2022-1900,1,2,23,59,59),17,125),"English 101")
    }

    fun createTempProgress(){
        val prog = student.classes[0].assignments[0].progress
        prog.addSession(20,currentTimeMillis()-300010000,currentTimeMillis()-300000000)
        prog.addSession(50,currentTimeMillis()-10000,currentTimeMillis())
    }

    /* creates assignment from uniqueString */
    fun createAssignmentUniqueString(uniqueString: String, className: String) {
        if(uniqueString.isBlank()) return
        val assignment = Assignment.fromUniqueString(uniqueString)
        addAssignment(assignment,className)
    }

    //calculates reading speed in pages per minute based on class if given, if class is not given bases it on all assignments
    fun calculateReadingSpeed(className: String?) {
        // TODO("Update this to work with reading sessions/activities is integrated into assignment/student")
        var total = 0.0
        var size = 0
        student.classes.forEach {
            val currentClass = it.name
            it.assignments.forEach {
                //TODO("Update once reading session finished to work with all sessions not just completed assignments")
                if((className != null && currentClass == className) || className == null) {
                    if (it.completed) {
                        total += (it.pageEnd - it.pageStart) / it.minutesSpend
                        size++
                    }
                }
            }
        }
        //calculates mean
        if (size != 0)
            student.readingSpeed = total/size
    }


    fun createSchedule() {
        if(student.readingSpeed == 0.0)
            error("Please complete reading session, so we can calculate reading speed")
        if(student.getUnfishedAssignments().isEmpty())
            error("Please enter an assignment")
        student.schedule = schedulePlanner.schedule
    }
}