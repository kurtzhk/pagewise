package com.pagewisegroup.pagewise

import android.os.Bundle
import android.view.View
import android.widget.EditText
import java.util.*
import kotlin.text.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private val usernameLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val validLoginLiveData = MediatorLiveData<Boolean>().apply {
            this.value = false
            addSource(usernameLiveData) { username ->
                val password = passwordLiveData.value
                this.value = formatLoginValidation(username, password)
            }
            addSource(passwordLiveData) { password ->
                val username = usernameLiveData.value
                this.value = formatLoginValidation(username,password)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //updates login relevant buttons
        val usernameInput = findViewById<TextInputLayout>(R.id.username)
        val passwordInput = findViewById<TextInputLayout>(R.id.password)
        val signInButton = findViewById<Button>(R.id.signInButton)

        usernameInput.editText?.doOnTextChanged { text, _, _, _ ->
            usernameLiveData.value = text?.toString()
        }

        passwordInput.editText?.doOnTextChanged { text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        validLoginLiveData.observe(this) { isValid ->
            signInButton.isEnabled = isValid
        }

        signInButton.setOnClickListener {
            //basic temp log in check
            Log.d("Login","Attempted to log in with username ${usernameLiveData.value}")
            if(usernameLiveData.value!!.contains("t")) {
                Log.d("Login","Logged in as teacher")
                //log in to student here
            } else if (usernameLiveData.value!!.contains("s")) {
                Log.d("Login","Logged in as student")
                //log in to teacher here
            }
            usernameInput.editText?.setText("")
            passwordInput.editText?.setText("")
            setContentView(R.layout.activity_main)
        }
    }
    //Checks if formatting is correct
    private fun formatLoginValidation(username: String?, password: String?) : Boolean {
        //would have formatting checks here
        val validUsername = username != null && username.isNotBlank()
        //val validpassword =
        return validUsername //&& validPassword
    }

    fun showDatePickerDialog(v: View) {
        if(v is DateDisplayView) {
            v.picker.show(supportFragmentManager, "datePicker")
        }

    }

    fun buildAssignment(v: View) {
        val name = findViewById<EditText>(R.id.assignmentName).text.toString()
        val due = findViewById<DateDisplayView>(R.id.assignmentDueDate)
        val pageStart = findViewById<EditText>(R.id.pageStart).text.toString().toInt()
        val pageEnd = findViewById<EditText>(R.id.pageEnd).text.toString().toInt()

        val assignment = Assignment(name, Date(due.picker.year, due.picker.month, due.picker.day), pageStart, pageEnd)
        print(assignment)
    }
}