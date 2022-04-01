package com.pagewisegroup.pagewise.recordReading

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pagewisegroup.pagewise.R
import com.pagewisegroup.pagewise.Student
import java.lang.System.currentTimeMillis

/**
 * Activity for uses to start a new live [ReadingSession]
 */
class ReadingActivity : AppCompatActivity() {
    private var midSession = false
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)

        val startEndButton = findViewById<Button>(R.id.start_end_button)

        val student = intent.getSerializableExtra("STUDENT") as Student
        val assignName = intent.getSerializableExtra("ASSIGN_NAME") as String
        val assignment = student.getAssignment(assignName)

        findViewById<TextView>(R.id.assn_name).text = assignment.name + "\nPage " + assignment.getProgress().getCurrentPage()
        startEndButton.setOnClickListener{
            //Finish reading ression
            if(midSession) {
                //go to separate view to enter final page number
                val intent = Intent(this, FinishReadingActivity::class.java).apply {
                    putExtra("pagewise.STIME",startTime)
                    putExtra("pagewise.ETIME", currentTimeMillis())
                    putExtra("pagewise.ASSN_NAME",assignName)
                    putExtra("STUDENT",student)
                }
                startActivity(intent)
            //Starts a new reading sessions
            } else{
                startTime = currentTimeMillis()
                midSession = true
            }
        }
    }
}