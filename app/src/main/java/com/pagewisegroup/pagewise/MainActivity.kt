package com.pagewisegroup.pagewise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.util.*
import kotlin.text.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        print(assignment)
    }
}