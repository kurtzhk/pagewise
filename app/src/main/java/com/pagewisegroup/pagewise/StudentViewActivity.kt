package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.util.Log
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

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

        //test for assignment entering via string
        val assignBtn = findViewById<Button>(R.id.string_test_button)
        assignBtn.setOnClickListener{
            setContentView(R.layout.activity_temp_enter_assign)
            val assignIdBtn = findViewById<Button>(R.id.assignID_button)
            val assignIdInput = findViewById<EditText>(R.id.assignID_input).text
            assignIdBtn.setOnClickListener {
                createAssignment(assignIdInput.toString())
            }
        }

    }

    /* creates assignment from uniqueString */
    fun createAssignment(uniqueString: String) {
        if(uniqueString.isBlank()) return
        val assignment = Assignment.fromUniqueString(uniqueString)
        Log.d("Assignment", assignment.toString())
    }
}