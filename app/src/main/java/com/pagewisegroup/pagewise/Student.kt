package com.pagewisegroup.pagewise

import android.content.Context
import android.util.Log
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null) : Serializable

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var reading_speed: Double, val context: Context, var id: Long? = null) : Serializable {
    val classes = ArrayList<PWClass>()
    var dbm: DatabaseManager? = null
    var schedule: SchedulePlanner? = null

    init {
        dbm = DatabaseManager(context)
        getClasses()
        createTempAssignments()
        reading_speed = 1.0 //TODO replaces once progress tracking is in place
        createSchedule()
    }

    //calculates reading speed in pages per minute based on class if given, if class is not given bases it on all assignments
    fun calculateReadingSpeed(className: String?) {
        // TODO("Update this to work with reading sessions/activities is integrated into assignment/student")
        var total = 0.0
        var size = 0
        classes.forEach {
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
            reading_speed = total/size
    }

    //list of all assignments
    fun getAllAssignments() : ArrayList<Assignment> {
        val assignments = ArrayList<Assignment>()
        classes.forEach {
            it.assignments.forEach {
                assignments.add(it)
            }
        }
        return assignments
    }

    //finds and returns all unfinished assignments
    fun unfishedAssignments(): ArrayList<Assignment> {
        val unfishedAssignments = ArrayList<Assignment>()
        classes.forEach {
            it.assignments.forEach {
                if(!it.completed)
                    unfishedAssignments.add(it)
            }
        }
        return unfishedAssignments
    }

    //gets all classes from db and adds them to class arraylist
    fun getClasses() {
        val db = dbm?.writableDatabase
        for(i in 1..dbm?.numberOfClasses(db)!!) {
            classes.add(dbm?.fetchClass(db,i.toLong())!!)
        }
    }

    //Returns the index of a class if it already exists, otherwise returns -1
    fun getClassIndex(name: String) : Int {
        var i = 0
        classes.forEach {
            i++
            if(it.name == name) return i
        }
        return -1
    }

    //Returns the index of a assignment if it already exists, otherwise returns -1
    fun getAssignIndex(name: String, pwClass: PWClass) : Int {
        var i = 0
        pwClass.assignments.forEach {
            i++
            if(it.name == name) return i
        }
        return -1
    }

    //adds a class both locally and to db
    fun addClass(name: String) {
        val db = dbm?.writableDatabase
        if(getClassIndex(name) > 0) { return }
        val pwClass = PWClass(name,ArrayList(),null)
        classes.add(pwClass)
        dbm?.recordClass(db, pwClass)
    }

    fun addAssignment(assignment: Assignment, className: String) {
        val db = dbm?.writableDatabase
        val pwClass = classes[getClassIndex(className)-1]
        if(getAssignIndex(assignment.name,pwClass) > 0) { return }
        pwClass.assignments.add(assignment)
        dbm?.recordAssignment(db,assignment,getClassIndex(className).toLong())
    }

    /* creates assignment from uniqueString */
    fun createAssignmentUniqueString(uniqueString: String) {
        if(uniqueString.isBlank()) return
        val assignment = Assignment.fromUniqueString(uniqueString)
        Log.d("Assignment", assignment.toString())
    }

    //temp for testing/demoing scheduling
    fun createTempAssignments() {
        //creates class
        addClass("CSCI 492")
        addClass("English 101")
        addClass("Bio 101")
        addAssignment(Assignment("Some CSCI Paper", Date(2022-1900,0,20,23,59,59),0,1000),"CSCI 492")
        addAssignment(Assignment("Kotlin Research", Date(2022-1900,0,21,23,59,59),5,53),"CSCI 492")
        addAssignment(Assignment("English Book #5", Date(2022-1900,0,22,23,59,59),17,125),"English 101")
    }

    fun createSchedule() {
        if(reading_speed == 0.0)
            error("Please complete assignment, so we can calculate reading speed")
        if(unfishedAssignments().isEmpty())
            error("Please enter an assignment")
        schedule = SchedulePlanner(unfishedAssignments(),reading_speed)
    }


    override fun toString(): String {
        val studentInfo = StringBuilder()
            .append("name: $name read speed: $reading_speed ppm id: $id\n")
        classes.forEach {
            studentInfo.append("Class ${it.name} \n\t Assignments: ")
            if(!it.assignments.isEmpty()) {
                for(i in 0 until (it.assignments.size-1)) {
                    studentInfo.append("${it.assignments[i].name}, ")
                }
                studentInfo.append("${it.assignments[it.assignments.size-1].name}\n")
            } else {
                studentInfo.append("N/A \n")
            }
        }
        return studentInfo.toString()
    }
}