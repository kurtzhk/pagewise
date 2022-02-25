package com.pagewisegroup.pagewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A fragment representing a list of Items.
 */
class AssignmentViewFragment() : Fragment() {

    private var columnCount = 1
    private var assignments: ArrayList<Assignment>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        assignments = requireActivity().intent.getSerializableExtra("CLASS_ASSIGNMENTS") as ArrayList<Assignment>?
        if (assignments == null) {
            assignments = (requireActivity().intent.getSerializableExtra("STUDENT") as Student).getAllAssignments()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_assignment_view_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = AssignmentRecyclerViewAdapter(assignments!!)
            }
        }
        return view
    }
}