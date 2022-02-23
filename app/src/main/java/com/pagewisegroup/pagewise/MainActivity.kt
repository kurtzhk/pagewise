/*
* Initializes app
* Displays "Title Page" of app
* Moves to profile page
* After 2 seconds or on click
* */

package com.pagewisegroup.pagewise

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.pagewisegroup.pagewise.profile.ProfileViewActivity
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var activityStarted = false;

        val intent = Intent(this, ProfileViewActivity::class.java)

        /*init logo*/
        val logo: ImageView = findViewById(R.id.pagewise_logo)
        logo.setImageResource(R.drawable.pagewise_logo)

        //After 3 seconds move to profile page
        Executors.newSingleThreadScheduledExecutor().schedule({
            if(!activityStarted)  startActivity(intent)
        }, 3, TimeUnit.SECONDS)

        //on click move to profile page
        val layout = findViewById<View>(R.id.loginView) as LinearLayout
        layout.setOnClickListener {
            startActivity(intent)
            activityStarted = true
        }
    }
}