package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import java.util.*

class FinishReadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish_reading)

        //TODO: get actual progress obj of relevant assignment
        //will need to get assn name from intent, look up in database
        val progressObj = Progress(Assignment("temp", Date(2022,3,19),1,100))

        val btn = findViewById<Button>(R.id.confirm_session_button)
        val num = findViewById<EditText>(R.id.lastpagenum)

        btn.setOnClickListener{
            //get entered number, check in range
            val n = num.text.toString().toIntOrNull()
            if(n != null && n >= progressObj.getCurrentPage() && n <= progressObj.assignment.pageEnd){
                //log session and return to student view
                //TODO: REPLACE 0 with current/start page
                progressObj.addSession(ReadingSession(0,n,intent.getLongExtra("pagewise.ETIME",1)-intent.getLongExtra("pagewise.STIME",0)))
                val intent = Intent(this, StudentViewActivity::class.java)
                startActivity(intent)
            }
            Log.d("Proggress", "Read to page $n")
        }
    }
}