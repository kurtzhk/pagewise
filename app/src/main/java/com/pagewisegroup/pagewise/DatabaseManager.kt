package com.pagewisegroup.pagewise

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
}
