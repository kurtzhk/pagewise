package com.pagewisegroup.pagewise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FinishReadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_reading)

        val student = intent.getSerializableExtra("STUDENT") as Student
        val aName = intent.getSerializableExtra("pagewise.ASSN_NAME") as String
        val progressObj = student.getAssignment(aName).progress

        val btn = findViewById<Button>(R.id.confirm_session_button)
        val num = findViewById<EditText>(R.id.lastpagenum)

        btn.setOnClickListener{
            //get entered number, check in range
            val n = num.text.toString().toIntOrNull()
            if(n != null && n >= progressObj.getCurrentPage() && n <= progressObj.assignment.pageEnd){
                //log session and return to student view
                progressObj.addSessionDB(this,student.id,n,intent.getLongExtra("pagewise.STIME",0),
                    intent.getLongExtra("pagewise.ETIME",1))
                val intent = Intent(this, StudentViewActivity::class.java).apply {
                    putExtra("STUDENT",student)
                }
                startActivity(intent)
            }
            Log.d("Progress", "Read to page $n")
        }
    }
}