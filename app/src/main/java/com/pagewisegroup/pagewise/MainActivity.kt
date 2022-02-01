package com.pagewisegroup.pagewise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.pagewisegroup.pagewise.profile.ProfileViewActivity

/*MAIN ACTIVITY
*
* this activity is the default set startup activity. it is a splash page that
* will ask the user if they are a teacher or student,
* then will start the LOGIN ACTIVITY*/
class MainActivity : AppCompatActivity() {
    val STUDENT_TOKEN = "STUDENT"
    val TEACHER_TOKEN = "TEACHER"
    private var login_group: String = STUDENT_TOKEN
    lateinit var studentButton: Button
    lateinit var teacherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_group = STUDENT_TOKEN
        //sendToLogin()
        val intent = Intent(this, ProfileViewActivity::class.java)

        /*init logo*/
        val logo: ImageView = findViewById(R.id.pagewise_logo)
        logo.setImageResource(R.drawable.pagewise_logo)

        /*init the buttons*/
        studentButton = findViewById<Button>(R.id.student_loginbutton)
        teacherButton = findViewById<Button>(R.id.teacher_loginbutton)

        /*apply button listeners*/
        studentButton.setOnClickListener {
            startActivity(intent)
        }
        /*apply button listeners*/
        teacherButton.setOnClickListener {
            login_group = TEACHER_TOKEN
            sendToLogin()
        }
    }

    /*YEET user to login*/
    private fun sendToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("LOGIN_GROUP", login_group)
        startActivity(intent)
    }
}