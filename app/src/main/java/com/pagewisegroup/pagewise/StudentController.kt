package com.pagewisegroup.pagewise

import android.content.Context
import android.util.Log
import java.lang.System.console
import java.lang.System.currentTimeMillis
import java.util.*
import kotlin.collections.ArrayList

class StudentController (context: Context, val student: Student) {
    private val dbm: DatabaseManager = DatabaseManager(context)
    private lateinit var schedulePlanner: SchedulePlanner

    init {
        if(student.classes.isEmpty()) fetchClasses() //avoid class duplication

        createTempAssignments() //for testing
        createTempProgress() //for testing charts & schedule
        student.printProgress() //temp
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
        Log.d("Reading session", "$assignName from $startPage - $endPage over ${(endTime-startTime)/60000} mins") //TEMP
        val progress = student.getAssignment(assignName).progress
        progress.addSession(endPage, startTime,endTime)
        dbm.recordSession(progress.getLastSession(),student.getAssignment(assignName).id!!, student.id!!)
        student.printProgress()
    }

    //temp for testing/demoing scheduling
    fun createTempAssignments() {
        //creates class
        addClass("CSCI 492")
        addClass("English 101")
        addClass("Bio 101")
        addAssignment(Assignment("Some CSCI Paper", Date(2022-1900,1,26,23,59,59),0,1000),"CSCI 492")
        addAssignment(Assignment("Kotlin Research", Date(2022-1900,1,28,23,59,59),5,53),"CSCI 492")
        addAssignment(Assignment("English Book #5", Date(2022-1900,2,2,23,59,59),17,125),"English 101")
    }

    //creates temp progress for testing
    fun createTempProgress(){
        val prog = student.classes[0].assignments[0].progress
        prog.addSession(20,currentTimeMillis()-301000000,currentTimeMillis()-300000000)
        prog.addSession(50,currentTimeMillis()-1500000,currentTimeMillis())
        prog.addSession(85,currentTimeMillis()-1500000,currentTimeMillis())
        prog.addSession(110,currentTimeMillis()-1500000,currentTimeMillis())
        prog.addSession(150,currentTimeMillis()-1500000,currentTimeMillis())
        student.classes[1].assignments[0].progress.addSession(50,currentTimeMillis()-1500000,currentTimeMillis())
    }

    /* creates assignment from uniqueString */
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
        Log.d("Global reading speed", totalReadingSpeed.toString()) //TEMP
        student.classes.forEach {
            val classReadingSpeed = calculateReadingSpeed(it.name)
            //if there is > 5 sessions adds classReading speed, else adds total
            if(classReadingSpeed == 0.0)
                student.readingSpeed.add(totalReadingSpeed)
            else
                student.readingSpeed.add(classReadingSpeed)
        }
    }

    //calculates reading speed for specific class if given else the global total
    fun calculateReadingSpeed(className: String?) : Double {
        var total = 0.0
        var size = 0
        student.classes.forEach {
            val currentClass = it.name
            it.assignments.forEach {
                if((className != null && currentClass == className) || className == null) {
                    it.progress.getSessions().forEach {
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
        if(size == 0 || (!className.isNullOrEmpty() && size < 5))
            return 0.0
        else
            return total/size
    }


    fun createSchedule() {
        //calculates reading speeds
        calculateReadingSpeeds()
        //creates schedule
        schedulePlanner = SchedulePlanner(student.getUnfishedAssignments(),student.getReadingSpeedByAssign())
        //sets student schedule
        student.schedule = schedulePlanner.schedule
    }
}