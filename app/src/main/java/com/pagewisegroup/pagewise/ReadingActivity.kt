package com.pagewisegroup.pagewise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import java.lang.System.currentTimeMillis

class ReadingActivity : AppCompatActivity() {
    var midSession = false
    var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)

        val startEndButton = findViewById<Button>(R.id.start_end_button)

        startEndButton.setOnClickListener{
            if(midSession){
                //go to separate view to enter final page number
                val intent = Intent(this,FinishReadingActivity::class.java).apply(){
                    putExtra("pagewise.STIME",startTime)
                    putExtra("pagewise.ETIME", currentTimeMillis())
                    //TODO: grab assn name from intent and pass it forward
                }
                startActivity(intent)
            }
            else{
                startTime = currentTimeMillis()
                midSession = true
            }
        }
    }
}