package com.pagewisegroup.pagewise.profile

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.pagewisegroup.pagewise.DatabaseManager
import com.pagewisegroup.pagewise.R
import com.pagewisegroup.pagewise.Student

class
ProfileViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view)
    }

    fun newProfile(v: View) {
        val enterName = AlertDialog.Builder(this)
        enterName.setTitle("Enter Profile Name:")
        enterName.setCancelable(false)
        val input = EditText(this)
        enterName.setView(input)
        enterName
            .setPositiveButton(
                "Submit",
            DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                val dbm = DatabaseManager(this)
                //Add new student to database
                dbm.recordStudent(Student(input.text.toString()))
                //Regenerate profile list
                supportFragmentManager.beginTransaction().replace(R.id.profileListContainer, ProfileViewFragment.newInstance()).commit()
                dialog.dismiss()
            })

        enterName
            .setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                })
        enterName.show()
    }
}