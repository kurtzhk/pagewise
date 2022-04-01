package com.pagewisegroup.pagewise.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pagewisegroup.pagewise.R
import com.pagewisegroup.pagewise.Student

/**
 * Fragment displaying error when one occurs
 */
class ErrorFragment : Fragment() {
    lateinit var student : Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_error, container, false)
        //gets error message from bundle and sets the text to it
        val bundle = this.arguments
        val errorMessage = bundle?.getString("errorMessage").toString()
        view.findViewById<TextView>(R.id.errorMessage).setText(errorMessage, TextView.BufferType.EDITABLE)
        return view
    }
}