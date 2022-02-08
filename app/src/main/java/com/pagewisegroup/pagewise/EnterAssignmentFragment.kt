package com.pagewisegroup.pagewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class EnterAssignmentFragment : Fragment() {
    lateinit var student : Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: remove when bundles added
        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_enter_assignment, container, false)
        //creates dropdown
        val dropdown = view.findViewById<AutoCompleteTextView>(R.id.class_choice)
        val arrayAdapter = ArrayAdapter(requireActivity().baseContext, R.layout.fragment_dropdown_item, student.getAllClassNames())
        dropdown.setAdapter(arrayAdapter)
        return view
    }
}