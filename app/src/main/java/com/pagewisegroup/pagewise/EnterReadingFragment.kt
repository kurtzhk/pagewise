package com.pagewisegroup.pagewise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class EnterReadingFragment : Fragment() {
    lateinit var student : Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_enter_reading, container, false)
        //get assignment
        val bundle = this.arguments
        val assignment = bundle?.getString("assignName")?.let { student.getAssignment(it) }
        //sets the assignment name
        view.findViewById<TextView>(R.id.assignementName).setText(assignment?.name, TextView.BufferType.EDITABLE)
        //sets start page to current page
        view.findViewById<EditText>(R.id.startPage).setText(assignment?.progress?.getCurrentPage().toString(), TextView.BufferType.EDITABLE)
        return view
    }
}