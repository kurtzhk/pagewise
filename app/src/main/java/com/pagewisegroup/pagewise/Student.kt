package com.pagewisegroup.pagewise

import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList

// id fields should only be populated when reading from or writing to database.
data class PWClass(val name: String, val assignments: ArrayList<Assignment> = ArrayList(), var id: Long? = null)

// Object that tracks a Student and their information including names, reading speed, and their classes.
class Student(var name: String, var reading_speed: Double, val context: Context, var id: Long? = null) {
    val classes = ArrayList<PWClass>()
    var dbm: DatabaseManager? = null
    var schedule: SchedulePlanner? = null

    init {
        dbm = DatabaseManager(context)
        getClasses()
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

    //print classes from DB -- temp for testing
    fun printDBClasses() {
        val db = dbm?.writableDatabase
        Log.d("Class number->",dbm?.numberOfClasses(db).toString())
        for(i in 1..dbm?.numberOfClasses(db)!!) {
            Log.d("DB class", dbm?.fetchClass(db,i.toLong())?.name.toString());
        }
    }
    //print assignments from DB -- temp for testing
    fun printDBAssign() {
        val db = dbm?.writableDatabase
        Log.d("Assignment number->",dbm?.numberOfAssignments(db).toString())
        for(i in 1..dbm?.numberOfAssignments(db)!!) {
            Log.d("DB assignment", dbm?.fetchAssignment(db,i.toLong())?.name.toString())
        }
    }

    //Returns the index of a class if it already exists, otherwise returns -1
    fun getClassIndex(name: String) : Int {
        var i = 0;
        classes.forEach {
            i++;
            if(it.name == name) return i
        }
        return -1
    }

    //Returns the index of a assignment if it already exists, otherwise returns -1
    fun getAssignIndex(name: String, pwClass: PWClass) : Int {
        var i = 0;
        pwClass.assignments.forEach {
            i++;
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

    //temp for testing/demoing scheduling
    fun createTempAssignments() {
        //creates class
        addClass("CSCI Pain")
        addClass("English 101")
        addClass("Bio 101")

        //finished assignments (to get reading speed)
        /*classes[0].assignments.add(Assignment("Assignment Here", Date(),0,25))
        classes[0].assignments[0].minutesSpend = 75.0
        classes[0].assignments[0].completed = true
        classes[0].assignments.add(Assignment("Assignment 2", Date(),50,100))
        classes[0].assignments[1].minutesSpend = 100.0
        classes[0].assignments[1].completed = true

        //unfinished assignments
        classes[0].assignments.add(Assignment("Assignment 3", Date(121,10,28,23,59),0,45))
        classes[0].assignments.add(Assignment("Assignment 4", Date(121,10,29,23,59),0,75))
        classes[0].assignments.get(3).currentPage = 25*/
        addAssignment(Assignment("Assignment one", Date(),0,25),"CSCI Pain")
        addAssignment(Assignment("Assignment two", Date(),0,25),"CSCI Pain")
        addAssignment(Assignment("Assignment Work", Date(),0,25),"English 101")

        //prints for testing
        //printDBClasses()
        //printClasses()
        //printDBAssign()
    }

    fun createSchedule() {
        if(reading_speed == 0.0)
            error("Please complete assignment, so we can calculate reading speed")
        if(unfishedAssignments().isEmpty())
            error("Please enter an assignment")
        schedule = SchedulePlanner(unfishedAssignments(),reading_speed)
    }

    fun printClasses() {
        classes.forEach {
            Log.d("Class ", it.name)
            it.assignments.forEach {
                Log.d("has assignment", it.name)
            }
        }
    }

    override fun toString(): String {
        return "name: $name read speed: $reading_speed ppm id: $id"
    }
}