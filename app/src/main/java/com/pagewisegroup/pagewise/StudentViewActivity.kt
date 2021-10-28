package com.pagewisegroup.pagewise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

class StudentViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_studentview)
    }

    /* creates assignment from id */
    fun createAssignment(id: String) {
        if(id.isBlank()) return
        val assignment = Assignment.fromId(id)
        Log.d("Assignment", assignment.toString())
    }
}