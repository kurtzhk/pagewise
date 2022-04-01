package com.pagewisegroup.pagewise.recordReading

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pagewisegroup.pagewise.*

/**
 * Activity for uses to finish there current/active [ReadingSession] and update [Progress]
 */
class FinishReadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_reading)

        val student = intent.getSerializableExtra("STUDENT") as Student
        val aName = intent.getSerializableExtra("pagewise.ASSN_NAME") as String
        val progressObj = student.getAssignment(aName).getProgress()

        val btn = findViewById<Button>(R.id.confirm_session_button)
        val num = findViewById<EditText>(R.id.lastpagenum)

        btn.setOnClickListener{
            //get entered number, check in range
            var n = num.text.toString().toIntOrNull()
            if(n != null && n >= progressObj.getCurrentPage()) {
                //To handle "over" reading
                if(n >= progressObj.assignment.pageEnd) {
                    n = progressObj.assignment.pageEnd
                    progressObj.assignment.setCompleted(true)
                }
                //log session and return to student view
                progressObj.addSessionDB(this,student.id,n,intent.getLongExtra("pagewise.STIME",0),
                    intent.getLongExtra("pagewise.ETIME",1))
                val intent = Intent(this, StudentViewActivity::class.java).apply {
                    putExtra("STUDENT",student)
                }
                startActivity(intent)
            } else {
                //error handling
                if(n == null) num.error = "Please enter a end page"
                else num.error = "End page should be after start page (${progressObj.getCurrentPage()})"
            }
        }
    }
}