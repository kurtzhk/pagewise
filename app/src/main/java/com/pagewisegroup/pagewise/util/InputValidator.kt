package com.pagewisegroup.pagewise.util

import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import com.pagewisegroup.pagewise.Student
import java.util.*

/**
 * Handles input validation and error checks
 */
class InputValidator {
    //checks if edit text is null, giving errText message if it is
    fun getEditTextHandler(text: EditText, errText: String) : String? {
        if(text.text.isNullOrEmpty()) {
            text.error = "Please enter $errText"
            return null
        }
        return text.text.toString().trim()
    }

    //checks if class with given name exists, error message if it does not
    fun getClassHandler(text: AutoCompleteTextView,student: Student) : String? {
        if(student.getClassIndex(text.text.toString()) < 0) {
            text.error = "Pick a class"
            return null
        }
        return text.text.toString()
    }

    //checks if date is null, error message if it is
    fun getDateHandler(dateDisp: DateDisplayView) : Date? {
        val date = Date(dateDisp.picker.year-1900, dateDisp.picker.month, dateDisp.picker.day)
        if(dateDisp.picker.month == 0 || dateDisp.picker.day == 0 || dateDisp.picker.day == 0) {
            dateDisp.error = "Pick a date"
            return null
        }
        return date
    }
    //checks if startPage is greater then end page giving error message if it is
    fun pageErrorCheck(text: TextView, startPage: Int, endPage: Int) : Boolean {
        if(startPage > endPage) {
            text.error = "Start page should be before end page"
            return false
        }
        return true
    }

    //checks if date is in the past, error message if not
    fun dateInPast(dateDisp: DateDisplayView, date: Date, errMsg: String) : Boolean{
        if(date.before(Date())) {
            dateDisp.error = errMsg
            return true
        }
        return false
    }
}