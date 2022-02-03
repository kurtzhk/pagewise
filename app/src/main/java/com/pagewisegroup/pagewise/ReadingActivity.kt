package com.pagewisegroup.pagewise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.lang.System.currentTimeMillis

class ReadingActivity : AppCompatActivity() {
    var midSession = false
    var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)

        val startEndButton = findViewById<Button>(R.id.start_end_button)
        val student = intent.getSerializableExtra("STUDENT") as Student
        val assignment = student.getAllAssignments()[0] //TODO: dropdown menu to select assignment

        //TODO: make this all look pretty
        findViewById<TextView>(R.id.assn_name).text = assignment.name + "\nPage " + assignment.progress.getCurrentPage()

        startEndButton.setOnClickListener{
            if(midSession){
                //go to separate view to enter final page number
                val intent = Intent(this,FinishReadingActivity::class.java).apply(){
                    putExtra("pagewise.STIME",startTime)
                    putExtra("pagewise.ETIME", currentTimeMillis())
                    putExtra("pagewise.PROGRESS",assignment.progress)
                    putExtra("STUDENT",student)
                    Log.d("End time in ms",""+currentTimeMillis())
                }
                startActivity(intent)
            } else{
                startTime = currentTimeMillis()
                Log.d("Start time in ms",""+startTime)
                midSession = true
            }
        }
    }
}