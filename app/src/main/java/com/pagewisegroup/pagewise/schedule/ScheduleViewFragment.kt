package com.pagewisegroup.pagewise.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pagewisegroup.pagewise.R
import com.pagewisegroup.pagewise.Student
import com.pagewisegroup.pagewise.util.ErrorFragment

/**
 * A fragment representing a list of [PlannedDay].
 */
class ScheduleViewFragment : Fragment() {

    private var columnCount = 1
    lateinit var student : Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        student = requireActivity().intent.getSerializableExtra("STUDENT") as Student
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_view_list, container, false)
        //Checks if empty and gives error message if there is
        if(student.schedule.isEmpty()) {
            val bundle = Bundle()
            bundle.putString("errorMessage", "Please add a non-overdue assignment")
            val fragment = ErrorFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .commit()
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
}