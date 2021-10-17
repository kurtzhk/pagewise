package com.pagewisegroup.pagewise

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DatePickerFragment] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {
    var month: Int = 0
    var day: Int = 0
    var year: Int = 0
    private var dateView: DateDisplayView? = null

    constructor(dateView: DateDisplayView) : this() {
        this.dateView = dateView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, y: Int, m: Int, d: Int) {
        month = m
        day = d
        year = y
        dateView?.updateText()
    }
}