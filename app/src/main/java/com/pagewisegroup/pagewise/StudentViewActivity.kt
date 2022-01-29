package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class StudentViewActivity : AppCompatActivity() {
    lateinit var studentController: StudentController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentview)

        //temp student for testing/demoing schedule
        studentController = StudentController(this)

        //generate toolbar with actions
        createStudentToolBar()
        //Prints info
        Log.d("Student Info", studentController.student.toString())

        //init and add all the fragments to the activity's fragment manager
        supportFragmentManager.commit {
            add(R.id.fragment_frame, StudentClassFragment())
            setReorderingAllowed(true)
        }
    }

    fun displayAssignmentView(){
        supportFragmentManager.commit {
            replace<AssignmentViewFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun showDatePickerDialog(v: View) {
        if(v is DateDisplayView) {
            v.picker.show(supportFragmentManager, "datePicker")
        }
    }

    fun buildAssignment(v: View) {
        val name = findViewById<EditText>(R.id.assignmentName).text.toString()
        val pickedClass = findViewById<AutoCompleteTextView>(R.id.class_choice).text.toString()
        val due = findViewById<DateDisplayView>(R.id.assignmentDueDate)
        val pageStart = findViewById<EditText>(R.id.pageStart).text.toString().toInt()
        val pageEnd = findViewById<EditText>(R.id.pageEnd).text.toString().toInt()

        val assignment = Assignment(name, Date(due.picker.year-1900, due.picker.month, due.picker.day), pageStart, pageEnd)
        studentController.addAssignment(assignment,pickedClass) //TODO: Make it go into correct class
    }

    fun buildClass(v: View) {
        val className = findViewById<TextInputLayout>(R.id.classNameInput).editText?.text.toString()
        studentController.addClass(className)
    }

    fun displayClassView(){
        supportFragmentManager.commit {
            replace<StudentClassFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayReadingView(){
        val intent = Intent(this, ReadingActivity::class.java)
        startActivity(intent)
    }

    //this should be moved to teach view later
    fun displayAssignmentEntry() {
        supportFragmentManager.commit {
            replace<EnterAssignment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayClassEntry(){
        supportFragmentManager.commit {
            replace<CreateClassFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayScheduleEntry(){
        supportFragmentManager.commit {
            replace<ScheduleViewFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    private fun createStudentToolBar() {
        //student taskbar actions are initialized here
        val studentActionsStrings = resources.getStringArray(R.array.StudentActions)
        //init spinner
        val spinner = findViewById<Spinner>(R.id.student_spinner)

        if (spinner != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, studentActionsStrings)
            spinner.adapter = adapter
        }

        //handle user actions on spinner
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //1=class view action
                //2=assignment view action
                //3=reading view(picks up on assignment user left off on
                //unless it is null or complete. then prompts user to pick one)
                when(position){
                    1 -> displayClassView()
                    2 -> displayAssignmentView()
                    3 -> displayReadingView()
                    4 -> displayAssignmentEntry()
                    5 -> displayClassEntry()
                    6 -> displayScheduleEntry()
                    else -> {
                        spinner.setSelection(0)
                    }
                }

                //reset array adapter's textview to empty string for a e s t h e t i c
                spinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
}