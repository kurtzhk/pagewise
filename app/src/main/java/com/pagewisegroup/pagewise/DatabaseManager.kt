package com.pagewisegroup.pagewise

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class DatabaseManager(val context: Context) : SQLiteOpenHelper(context, "PagewiseDB", null, 1){

    // Creates the tables for our database. Assignments, classes, students,
    // and a join table between students and classes.
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS ASSIGNMENTS(" +
                "assignment_id INTEGER," +
                "name TEXT," +
                "class_id INT NOT NULL," + //links assignments to their class.
                "due_date INT NOT NULL," + //will be stored in unix epoch time.
                "page_start INT NOT NULL," +
                "page_end INT NOT NULL," +
                "time_to_complete REAL," + /*unsure if this is needed, might decide to store reading
                                       speed in student table and just dynamically calculate
                                       ttc at runtime. need feedback. */
                "completed BOOLEAN NOT NULL," + //if true t_t_c will be considered actual ttc otherwise an estimate.
                "PRIMARY KEY(assignment_id ASC))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS CLASSES(" +
                "class_id INTEGER," +
                "name TEXT," +
                "PRIMARY KEY(class_id ASC))")
        db?.execSQL("CREATE TABLE IF NOT EXISTS STUDENTS(" +
                "student_id INTEGER," +
                "name TEXT," + //do we need to store names? not sure if it's helpful.
                "PRIMARY KEY(student_id ASC))")
        // join table facilitates many to many relationship of classes and students.
        db?.execSQL("CREATE TABLE IF NOT EXISTS ENROLLMENTS(" +
                "student_id INT NOT NULL," +
                "class_id INT NOT NULL," +
                "PRIMARY KEY(student_id, class_id))")
        // reading session table
        db?.execSQL("CREATE TABLE IF NOT EXISTS SESSIONS(" +
                "session_id INTEGER," +
                "assignment_id INTEGER," +
                "student_id INTEGER," +
                "page_start INTEGER," +
                "page_end INTEGER," +
                "start_time INTEGER," +
                "end_time INTEGER," +
                "PRIMARY KEY(session_id)")
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
    fun recordAssignment(assignment: Assignment, classID: Long) {
        val values = ContentValues()
        values.put("name", assignment.name)
        values.put("class_id", classID)
        values.put("due_date", assignment.dueDate.time)
        values.put("page_start", assignment.pageStart)
        values.put("page_end", assignment.pageEnd)
        values.put("time_to_complete", assignment.hoursToComplete)
        values.put("completed", assignment.completed)
        if (assignment.id != null) {
            writableDatabase?.update("ASSIGNMENTS", values, "assignment_id = ${assignment.id}", null)
        } else {
            assignment.id = writableDatabase?.insert("ASSIGNMENTS", null, values)
        }
    }

    // returns class id.
    // inserts or updates class to table depending on if it has been recorded yet.
    fun recordClass(pwClass: PWClass): Long {
        val values = ContentValues()
        values.put("name", pwClass.name)
        if (pwClass.id != null) {
            writableDatabase?.update("CLASSES", values, "class_id = ${pwClass.id}", null)
        } else {
            pwClass.id = writableDatabase?.insert("CLASSES", null, values)
        }
        for (a in pwClass.assignments) {
            recordAssignment(a, pwClass.id!!) //should already be non null as long as the insert
                                            //succeeded. we want to throw an exception otherwise.
        }
        return pwClass.id!!
    }

    // inserts or updates student to table depending on if it has been recorded yet.
    // also updates enrollment table.
    fun recordStudent(student: Student) {
        val values = ContentValues()
        values.put("name", student.name)
        if (student.id != null) {
            writableDatabase?.update("STUDENTS", values, "student_id = ${student.id}", null)
        } else {
            student.id = writableDatabase?.insert("STUDENTS", null, values)
        }
        values.clear()
        values.put("student_id", student.id)
        // this is hacky, there is probably a better solution but I am trying to avoid the problem
        // of duplicate enrollments as well as dropping enrollments from the database if they don't
        // exist anymore. Maybe a method in student that handles dropping classes that also manages
        // the DB? I have to think about it.
        writableDatabase?.delete("ENROLLMENTS", "student_id = ${student.id}", null)
        for (c in student.classes) {
            val cID: Long = recordClass(c)
            values.put("class_id", cID)
            writableDatabase?.insert("ENROLLMENTS", null, values)
            values.remove("class_id")
        }
    }

    fun fetchStudent(id: Long): Student {
        val sTable = readableDatabase?.rawQuery("SELECT name FROM STUDENTS WHERE student_id = $id", null)
        val eTable = readableDatabase?.rawQuery("SELECT class_id FROM ENROLLMENTS WHERE student_id = $id", null)
        sTable?.moveToFirst()
        val name = sTable?.getString(0)
        val s = Student(name!!, id)
        if (eTable?.moveToFirst() == true) {
            do {
                s.classes.add(fetchClass(eTable.getLong(0)))
            } while (eTable.moveToNext())
        }
        return s
    }

    fun numberOfClasses(db: SQLiteDatabase?) : Int {
        val cTable = db?.rawQuery("SELECT class_id FROM CLASSES", null) ?: return 0
        return cTable.count
    }

    fun numberOfAssignments(db: SQLiteDatabase?) : Int {
        val ATable = db?.rawQuery("SELECT class_id FROM ASSIGNMENTS", null) ?: return 0
        return ATable.count
    }

    fun fetchClass(id: Long): PWClass {
        val cTable = readableDatabase?.rawQuery("SELECT name FROM CLASSES WHERE class_id = $id", null)
        val aTable = readableDatabase?.rawQuery("SELECT assignment_id FROM ASSIGNMENTS WHERE class_id = $id", null)
        cTable?.moveToFirst()
        val assignments = ArrayList<Assignment>()
        val name = cTable?.getString(0)
        if (aTable?.moveToFirst() == true) {
            do {
                assignments.add(fetchAssignment(aTable.getLong(0))!!)
            } while (aTable.moveToNext())
        }
        return PWClass(name!!, assignments, id)
    }

    fun fetchAssignment(id: Long): Assignment? {
        val aTable = readableDatabase?.rawQuery(
            "SELECT name,due_date,page_start,page_end,time_to_complete,completed FROM ASSIGNMENTS WHERE assignment_id = $id",
            null
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

    //Prints the database to the log for debugging.
    fun logDatabase() {
        val aTable = readableDatabase?.rawQuery("SELECT * FROM ASSIGNMENTS", null)
        val cTable = readableDatabase?.rawQuery("SELECT * FROM CLASSES", null)
        val sTable = readableDatabase?.rawQuery("SELECT * FROM STUDENTS", null)
        val eTable = readableDatabase?.rawQuery("SELECT * FROM ENROLLMENTS", null)
        Log.d("Assignment Table", cursorToString(aTable!!))
        Log.d("Class Table", cursorToString(cTable!!))
        Log.d("Student Table", cursorToString(sTable!!))
        Log.d("Enrollment Table", cursorToString(eTable!!))
        aTable.close()
        cTable.close()
        sTable.close()
        eTable.close()
    }
    private fun cursorToString(c: Cursor): String {
        val out = StringBuilder()
        out.append("--TABLE--\n")
        for (i in 0 until c.columnCount) {
            c.moveToFirst()
            out.append(c.getColumnName(i)).append(": ")
            do {
                out.append(c.getString(i)).append(", ")
            } while (c.moveToNext())
            out.append("\n")
        }
        return out.toString()
    }
}
