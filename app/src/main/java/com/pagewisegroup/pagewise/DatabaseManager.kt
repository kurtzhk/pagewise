package com.pagewisegroup.pagewise

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, "PagewiseDB", null, 1){

    // Creates the tables for our database. Assignments, classes, students,
    // and a join table between students and classes.
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ASSIGNMENTS(" +
                "assignment_id INT," +
                "name TEXT," +
                "class_id INT NOT NULL," + //links assignments to their class.
                "due_date INT NOT NULL," + //will be stored in unix epoch time.
                "page_start INT NOT NULL," +
                "page_end INT NOT NULL," +
                "time_to_complete REAL," + /*unsure if this is needed, might decide to store reading
                                       speed in student table and just dynamically calculate
                                       ttc at runtime. need feedback. */
                "completed BOOLEAN NOT NULL," + //if true t_t_c will be considered actual ttc otherwise an esitmate.
                "PRIMARY KEY(assignment_id ASC))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS CLASSES(" +
                "class_id INTEGER," +
                "name TEXT," +
                "PRIMARY KEY(class_id ASC))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS STUDENTS(" +
                "student_id INTEGER," +
                "name TEXT," + //do we need to store names? not sure if it's helpful.
                "read_speed REAL," + //pages per hour
                "PRIMARY KEY(student_id ASC))")
        // join table facilitates many to many relationship of classes and students.
        db?.execSQL("CREATE TABLE IF NOT EXISTS ENROLLMENTS(" +
                "student_id INT NOT NULL," +
                "class_id INT NOT NULL," +
                "PRIMARY KEY(student_id, class_id))")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        //clear db.
        db?.execSQL("DROP TABLE IF EXISTS ASSIGNMENTS")
        db?.execSQL("DROP TABLE IF EXISTS CLASSES")
        db?.execSQL("DROP TABLE IF EXISTS STUDENTS")
        db?.execSQL("DROP TABLE IF EXISTS ENROLLMENTS")
        //reconstruct db.
        onCreate(db)
    }

    // inserts or updates student to table depending on if it has been recorded yet.
    private fun recordAssignment(db: SQLiteDatabase?, assignment: Assignment, classID: Long) {
        val values = ContentValues()
        values.put("name", assignment.name)
        values.put("class_id", classID)
        values.put("due_date", assignment.dueDate.time)
        values.put("page_start", assignment.pageStart)
        values.put("page_end", assignment.pageEnd)
        values.put("time_to_complete", assignment.hoursToComplete)
        values.put("completed", assignment.completed)
        if (assignment.id != null) {
            db?.update("ASSIGNMENTS", values, "assignment_id = ${assignment.id}", null)
        } else {
            assignment.id = db?.insert("ASSIGNMENTS", null, values)
        }
    }

    // returns class id.
    // inserts or updates class to table depending on if it has been recorded yet.
    fun recordClass(db: SQLiteDatabase?, pwClass: PWClass): Long {
        val values = ContentValues()
        values.put("name", pwClass.name)
        if (pwClass.id != null) {
            db?.update("CLASSES", values, "class_id = ${pwClass.id}", null)
        } else {
            pwClass.id = db?.insert("CLASSES", null, values)
        }
        for (a in pwClass.assignments) {
            recordAssignment(db, a, pwClass.id!!) //should already be non null as long as the insert
                                            //succeeded. we want to throw an exception otherwise.
        }
        return pwClass.id!!
    }

    // inserts or updates student to table depending on if it has been recorded yet.
    // also updates enrollment table.
    fun recordStudent(db: SQLiteDatabase?, student: Student) {
        val values = ContentValues()
        values.put("name", student.name)
        values.put("read_speed", student.reading_speed)
        if (student.id != null) {
            db?.update("STUDENTS", values, "student_id = ${student.id}", null)
        } else {
            student.id = db?.insert("STUDENTS", null, values)
        }
        values.clear()
        values.put("student_id", student.id)
        // this is hacky, there is probably a better solution but I am trying to avoid the problem
        // of duplicate enrollments as well as dropping enrollments from the database if they don't
        // exist anymore. Maybe a method in student that handles dropping classes that also manages
        // the DB? I have to think about it.
        db?.delete("ENROLLMENTS", "student_id = ${student.id}", null)
        for (c in student.classes) {
            val cID: Long = recordClass(db, c)
            values.put("class_id", cID)
            db?.insert("ENROLLMENTS", null, values)
            values.remove("class_id")
        }
    }

    fun fetchStudent(db: SQLiteDatabase?, id: Long): Student {
        val sTable = db?.rawQuery("SELECT name, read_speed FROM STUDENTS WHERE student_id = $id", null)
        val eTable = db?.rawQuery("SELECT class_id FROM ENROLLMENTS WHERE student_id = $id", null)
        sTable?.moveToFirst()
        val name = sTable?.getString(0)
        val readingSpeed = sTable?.getDouble(1)
        val s = Student(name!!, readingSpeed!!, id)
        if (eTable?.moveToFirst() == true) {
            do {
                s.classes.add(fetchClass(db, eTable.getLong(0)))
            } while (eTable.moveToNext())
        }
        return s
    }

    private fun fetchClass(db: SQLiteDatabase?, id: Long): PWClass {
        val cTable = db?.rawQuery("SELECT name FROM CLASSES WHERE class_id = $id", null)
        val aTable = db?.rawQuery("SELECT assignment_id FROM ASSIGNMENTS WHERE class_id = $id", null)
        cTable?.moveToFirst()
        val assignments = ArrayList<Assignment>()
        val name = cTable?.getString(0)
        if (aTable?.moveToFirst() == true) {
            do {
                assignments.add(fetchAssignment(db, aTable.getLong(0))!!)
            } while (aTable.moveToNext())
        }
        return PWClass(name!!, assignments, id)
    }

    private fun fetchAssignment(db: SQLiteDatabase?, id: Long): Assignment? {
        val columns = arrayOf("name", "due_date", "page_start", "page_end", "time_to_complete", "completed")
        val aTable = db?.rawQuery(
            "SELECT ?0, ?1, ?2, ?3, ?4, ?5 FROM ASSIGNMENTS WHERE assignment_id = $id",
            columns
        )
        if (aTable?.moveToFirst() == true) {
            val name = aTable.getString(0)
            val dueDate = aTable.getLong(1)
            val pageStart = aTable.getInt(2)
            val pageEnd = aTable.getInt(3)
            val timeToComplete = aTable.getDouble(4)
            val completed = aTable.getInt(5) == 1
            val a = Assignment(name, Date(dueDate), pageStart, pageEnd)
            a.hoursToComplete = timeToComplete
            a.completed = completed
            return a
        }
        return null
    }
}
