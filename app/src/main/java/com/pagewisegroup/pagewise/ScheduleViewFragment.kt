package com.pagewisegroup.pagewise

import android.os.Bundle
import android.util.Log
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
class ScheduleViewFragment : Fragment() {

    private var columnCount = 1
    lateinit var student : Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_assignment_view_list, container, false)
        //Checks if empty and gives error message if there is
        if(student.schedule.isEmpty()) {
            val bundle = Bundle()
            bundle.putString("errorMessage", "Please add a non-overdue assignment")
            val fragment = ErrorFragment()
            fragment.setArguments(bundle)

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .commit();
            return view
        }
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ScheduleRecyclerViewAdapter(student.getScheduleByAssignment())
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ScheduleViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}