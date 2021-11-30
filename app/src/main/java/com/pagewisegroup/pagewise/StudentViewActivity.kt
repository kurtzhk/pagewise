package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class StudentViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentview)

        //temp student for testing/demoing schedule
        val student = Student("Test student", 0.0, null)
        student.createTempAssignments()
        student.calculateReadingSpeed()

        //generate toolbar with actions
        createStudentToolBar()

        //Prints info
        Log.d("Student Info", student.toString())
        student.createSchedule()
        Log.d("Schedule", student.schedule.toString())

        //init and add all the fragments to the activity's fragment manager
        supportFragmentManager.commit {
            add(R.id.fragment_frame, StudentClassFragment())
            setReorderingAllowed(true)
        }

        //all this nonsense is just for my testing purposes
//        val btn = findViewById<Button>(R.id.atest_button)
//        btn.setOnClickListener{
//            val intent = Intent(this, ReadingActivity::class.java)
//            //for final implementation, must add assignment name to intent
//            startActivity(intent)
//        }

        //test for assignment entering via string
//        val assignBtn = findViewById<Button>(R.id.string_test_button)
//        assignBtn.setOnClickListener{
//            setContentView(R.layout.activity_temp_enter_assign)
//            val assignIdBtn = findViewById<Button>(R.id.assignID_button)
//            val assignIdInput = findViewById<EditText>(R.id.assignID_input).text
//            assignIdBtn.setOnClickListener {
//                createAssignment(assignIdInput.toString())
//            }
//        }
    }

    /* creates assignment from uniqueString */
    fun createAssignment(uniqueString: String) {
        if(uniqueString.isBlank()) return
        val assignment = Assignment.fromUniqueString(uniqueString)
        Log.d("Assignment", assignment.toString())
    }

    fun displayAssignmentView(){
        supportFragmentManager.commit {
            replace<AssignmentViewFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
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

    private fun createStudentToolBar(){
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
                    else -> {
                        spinner.setSelection(0)
                    }
                }

                //reset arrayadapter's textview to empty string for a e s t h e t i c
                spinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }
}