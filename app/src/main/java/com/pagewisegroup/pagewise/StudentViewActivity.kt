package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StudentViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentview)

        //all this nonsense is just for my testing purposes
        val btn = findViewById<Button>(R.id.atest_button)
        btn.setOnClickListener{
            val intent = Intent(this, ReadingActivity::class.java)
            //for final implementation, must add assignment name to intent
            startActivity(intent)
        }
    }
}