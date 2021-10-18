package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import java.util.*

/* LOGIN ACTIVITY
*
* this activity is responsible for validating a user's login attempt, and for communication
* with the server about the login attempt.
*
* RECEIVED FROM BUNDLE:
* LOGIN_GROUP: determines if the login is coming from a student or professor.
* TODO: like everything lol*/

class LoginActivity : AppCompatActivity() {
    val STUDENT_TOKEN = "STUDENT"
    val TEACHER_TOKEN = "TEACHER"
    private val usernameLiveData = MutableLiveData<String>()
    private val passwordLiveData = MutableLiveData<String>()
    private val validLoginLiveData = MediatorLiveData<Boolean>().apply {
        this.value = false
        addSource(usernameLiveData) { username ->
            val password = passwordLiveData.value
            this.value = validateSignOn(username, password)
        }
        addSource(passwordLiveData) { password ->
            val username = usernameLiveData.value
            this.value = validateSignOn(username, password)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*unpack intent*/
        val login_group: String? = intent.getStringExtra("LOGIN_GROUP")
        setContentView(R.layout.login)

        /*init buttons*/
        val usernameInput = findViewById<TextInputLayout>(R.id.username_input)
        val passwordInput = findViewById<TextInputLayout>(R.id.password_input)
        val signInButton = findViewById<Button>(R.id.signInButton)

        /*when user commits to login*/
        signInButton.setOnClickListener {

            /*yeet user to corresponding activity after validating*/
            Log.d("Login", "Attempted to log in with username ${usernameLiveData.value}")

            /*TODO: validate and throw errors*/
            if(!validateSignOn(usernameLiveData.toString(), passwordLiveData.toString())){

            }

            if (login_group.equals(STUDENT_TOKEN)) {
                val intent = Intent(this, StudentViewActivity::class.java)
                startActivity(intent)
            } else if (login_group.equals(TEACHER_TOKEN)) {
                val intent = Intent(this, TeacherViewActivity::class.java)
                startActivity(intent)
            }
        }

        usernameInput.editText?.doOnTextChanged { text, _, _, _ ->
            usernameLiveData.value = text?.toString()
        }

        passwordInput.editText?.doOnTextChanged { text, _, _, _ ->
            passwordLiveData.value = text?.toString()
        }

        validLoginLiveData.observe(this) { isValid ->
            signInButton.isEnabled = isValid
        }
    }

    /*validateSignOn
    * validates the formatting of the username that is entered,
    * cleans the username/password, before requesting sign on
    * with entered password within selected group[teacher/student]
    *
    * TODO: additional formatting validations and needs input cleansing*/
    private fun validateSignOn(username: String?, password: String?): Boolean {

        return username != null && username.isNotBlank() && password != null && password.isNotBlank()
    }
}