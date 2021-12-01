package com.pagewisegroup.pagewise

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import java.util.*
import kotlin.collections.ArrayList

/* TEACHER VIEW ACTIVITY
*
* this activity is responsible for validating a user's login attempt, and for communication
* with the server about the login attempt.
*
* RECEIVED FROM BUNDLE:
* LOGIN_GROUP: determines if the login is coming from a student or professor.
* TODO: like everything lol*/
class TeacherViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        s.classes.add(PWClass("test_class"))
        setContentView(R.layout.activity_teacherview)
    }

    fun showDatePickerDialog(v: View) {
        if(v is DateDisplayView) {
            v.picker.show(supportFragmentManager, "datePicker")
        }

    }

    fun buildAssignment(v: View) {
        val name = findViewById<EditText>(R.id.assignmentName).text.toString()
        val due = findViewById<DateDisplayView>(R.id.assignmentDueDate)
        val pageStart = findViewById<EditText>(R.id.pageStart).text.toString().toInt()
        val pageEnd = findViewById<EditText>(R.id.pageEnd).text.toString().toInt()

        val assignment = Assignment(name, Date(due.picker.year, due.picker.month, due.picker.day), pageStart, pageEnd)
        Log.d("Assignment", assignment.toString())
        demoDatabaseRecording(assignment)
    }

    // Temporary function to show database working.
    private val s = Student("test_student", 0.0, this)
    private fun demoDatabaseRecording(a: Assignment) {

        Log.d("Student", s.toString())

        s.classes[0].assignments.add(a)
        Log.d("Class", s.classes[0].toString())
        val dbm = DatabaseManager(this)
        val db: SQLiteDatabase? = dbm.writableDatabase
        dbm.recordStudent(db, s)
        val aTable = db?.rawQuery("SELECT * FROM ASSIGNMENTS", null)
        val cTable = db?.rawQuery("SELECT * FROM CLASSES", null)
        val sTable = db?.rawQuery("SELECT * FROM STUDENTS", null)
        val eTable = db?.rawQuery("SELECT * FROM ENROLLMENTS", null)
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