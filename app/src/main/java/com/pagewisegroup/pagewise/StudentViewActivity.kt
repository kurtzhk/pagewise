package com.pagewisegroup.pagewise

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.textfield.TextInputLayout
import com.pagewisegroup.pagewise.assignmentView.AssignmentViewFragment
import com.pagewisegroup.pagewise.classView.StudentClassFragment
import com.pagewisegroup.pagewise.recordReading.RecordReadingFragment
import com.pagewisegroup.pagewise.schedule.ScheduleViewFragment
import com.pagewisegroup.pagewise.util.DateDisplayView
import com.pagewisegroup.pagewise.util.InputValidator
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

/**
 * Displays information about student through UI
 */
class StudentViewActivity : AppCompatActivity() {
    private lateinit var studentController: StudentController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentview)
        studentController = StudentController(this, intent.getSerializableExtra("STUDENT") as Student)

        //generate toolbar with actions
        createStudentToolBar()

        //init and add all the fragments to the activity's fragment manager
        supportFragmentManager.commit {
            add(R.id.fragment_frame, StudentClassFragment())
            setReorderingAllowed(true)
        }
    }

    //Displays date picker
    fun showDatePickerDialog(v: View) {
        if(v is DateDisplayView) {
            v.picker.show(supportFragmentManager, "datePicker")
        }
    }

    //Builds assignment for UI information
    fun buildAssignment(v: View) {
        val inputValidator = InputValidator()
        val name = inputValidator.getEditTextHandler(findViewById(R.id.assignmentName), "name")
        val pickedClass = inputValidator.getClassHandler(findViewById(R.id.class_choice), studentController.student)
        val due = inputValidator.getDateHandler(findViewById(R.id.assignmentDueDate))
        val pageStart = inputValidator.getEditTextHandler(findViewById(R.id.pageStart), "start page")
        val pageEnd = inputValidator.getEditTextHandler(findViewById(R.id.pageEnd), "end page")

        //error checks
        if(name.isNullOrEmpty() || pickedClass.isNullOrEmpty() || pageStart.isNullOrEmpty() || pageEnd.isNullOrEmpty() || due == null) return
        if(inputValidator.dateInPast(findViewById(R.id.assignmentDueDate),due,"Due date must be in the future")) return
        if(!inputValidator.pageErrorCheck(findViewById(R.id.pageStart),pageStart.toInt(),pageEnd.toInt())) return

        val assignment = Assignment(name, due, pageStart.toInt(), pageEnd.toInt())
        studentController.addAssignment(assignment,pickedClass)
        displayClassView()
    }

    //Builds class from UI information
    fun buildClass(v: View) {
        val inputValidator = InputValidator()
        val className = inputValidator.getEditTextHandler(findViewById<TextInputLayout>(R.id.classNameInput).editText!!, "class name")
        if(className.isNullOrEmpty()) return
        studentController.addClass(className)
        displayClassView()
    }

    //Builds reading session from UI information
    @RequiresApi(Build.VERSION_CODES.M)
    fun buildReadingSession(v: View) {
        val inputValidator = InputValidator()
        val assignmentName = findViewById<TextView>(R.id.assignementName).text.toString()
        val startPage = findViewById<TextView>(R.id.startPage).text.toString().toInt()
        var endPage = inputValidator.getEditTextHandler(findViewById(R.id.endPage), "end page")
        val addedTime = inputValidator.getEditTextHandler(findViewById(R.id.sessionTime), "time spent")
        val date = inputValidator.getDateHandler(findViewById(R.id.readingDate))
        val time = findViewById<TimePicker>(R.id.timePicker)

        //error checks
        if(endPage.isNullOrEmpty() || addedTime.isNullOrEmpty() || addedTime.isNullOrEmpty() || date == null) return
        if(!inputValidator.dateInPast(findViewById(R.id.readingDate),date,"Please record past reading")) return
        if(!inputValidator.pageErrorCheck(findViewById(R.id.endPage), startPage,endPage.toInt())) return
        if(endPage.toInt() >= studentController.student.getAssignment(assignmentName).pageEnd) {
            endPage = studentController.student.getAssignment(assignmentName).pageEnd.toString()
            studentController.student.getAssignment(assignmentName).setCompleted(true)
        }

        //converts date && time to milliseconds
        val formattedDate = "${date.year-1900}/${date.month}/${date.date} ${time.hour}:${time.minute}:00"
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val startTime = sdf.parse(formattedDate).time
        val endTime = startTime + addedTime.toLong()*60000
        //adds reading session to assignment
        studentController.addReadingSession(assignmentName,startPage,endPage.toInt(),startTime,endTime)
        displayClassView()
    }

    //Creates toolbar to allow users to navigate the app
    private fun createStudentToolBar() {
        //student taskbar actions are initialized here
        val studentActionsStrings = resources.getStringArray(R.array.StudentActions)
        //init spinner
        val spinner = findViewById<Spinner>(R.id.student_spinner)
        findViewById<TextView>(R.id.student_name_display).text = studentController.student.name

        if (spinner != null) {
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, studentActionsStrings)
            spinner.adapter = adapter
        }

        //handle user actions on spinner
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position){
                    1 -> displayClassView()
                    2 -> displayAssignmentView()
                    3 -> displayReadingView()
                    4 -> displayAssignmentEntry()
                    5 -> displayClassEntry()
                    6 -> displayScheduleEntry()
                    7 -> displayChartsView()
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

    //Displays relevant fragment controlled by navigation bar

    fun displayClassView() {
        //updates reading speed (and time to complete)
        studentController.calculateReadingSpeeds()
        supportFragmentManager.commit {
            replace<StudentClassFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayReadingView() {
        //updates reading speed (and time to complete)
        studentController.calculateReadingSpeeds()
        supportFragmentManager.commit {
            replace<RecordReadingFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayAssignmentEntry() {
        supportFragmentManager.commit {
            replace<EnterAssignmentFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayAssignmentView(assignments: ArrayList<Assignment>? = null){
        supportFragmentManager.commit {
            intent.removeExtra("CLASS_ASSIGNMENTS")
            intent.putExtra("CLASS_ASSIGNMENTS", assignments)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("assignments")
                replace<AssignmentViewFragment>(R.id.fragment_frame)
            }
        }
    }

    fun displayClassEntry() {
        supportFragmentManager.commit {
            replace<CreateClassFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayScheduleEntry(){
        //updates schedule or creates one if it does not exist
        studentController.createSchedule()
        supportFragmentManager.commit {
            replace<ScheduleViewFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }

    fun displayChartsView(){
        supportFragmentManager.commit {
            replace<ChartViewFragment>(R.id.fragment_frame)
            setReorderingAllowed(true)
        }
    }
}