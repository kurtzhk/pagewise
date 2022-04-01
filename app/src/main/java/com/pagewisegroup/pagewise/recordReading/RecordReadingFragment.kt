package com.pagewisegroup.pagewise.recordReading

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.pagewisegroup.pagewise.R
import com.pagewisegroup.pagewise.Student

/**
 * Fragment displaying [PWClass] and [Assignment]
 * Allows user to pick between live and past reading session to record
 */
class RecordReadingFragment : Fragment() {
    private lateinit var student : Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_record_reading_picker, container, false)
        //classes dropdown
        val classDropdown = view.findViewById<AutoCompleteTextView>(R.id.choiceClassReading)
        val classArrayAdapter = ArrayAdapter(requireActivity().baseContext,
            R.layout.fragment_dropdown_item, student.getAllClassNames())
        classDropdown.setAdapter(classArrayAdapter)
        //assignment dropdown
        val assignDropdown = view.findViewById<AutoCompleteTextView>(R.id.choiceAssignmentReading)
        //updates on class choice
        classDropdown.setOnItemClickListener { _, view, _, _ ->
            updateAssignDropdown(view,classDropdown,assignDropdown)
        }

        //to create reading session now
        view.findViewById<MaterialButton>(R.id.timeYourselfButton).setOnClickListener {
            //checks to see if class exists
            if(student.assignExists(classDropdown.text.toString(),assignDropdown.text.toString())) {
                val intent = Intent(requireActivity().baseContext, ReadingActivity::class.java)
                intent.putExtra("STUDENT",student)
                intent.putExtra("ASSIGN_NAME",assignDropdown.text.toString())
                startActivity(intent)
            }
        }

        //to record past completed reading assignment
        view.findViewById<MaterialButton>(R.id.pastReadingButton).setOnClickListener {
            if(student.assignExists(classDropdown.text.toString(),assignDropdown.text.toString())) {
                //puts assignment name in bundle
                val bundle = Bundle()
                bundle.putString("assignName", assignDropdown.text.toString())
                val fragment = EnterReadingFragment()
                fragment.arguments = bundle

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_frame, fragment)
                    .commit()
            }
        }

        return view
    }

    //updates assignment dropdown based on class dropdown
    private fun updateAssignDropdown(view: View, classDropdown: AutoCompleteTextView, assignDropdown: AutoCompleteTextView) {
        val assignArrayAdapter = ArrayAdapter(requireActivity().baseContext,
            R.layout.fragment_dropdown_item, student.getAssignNames(classDropdown.text.toString()))
        assignDropdown.setAdapter(assignArrayAdapter)
    }
}