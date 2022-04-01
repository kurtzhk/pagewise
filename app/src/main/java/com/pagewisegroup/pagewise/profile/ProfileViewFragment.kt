package com.pagewisegroup.pagewise.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pagewisegroup.pagewise.DatabaseManager
import com.pagewisegroup.pagewise.R
import com.pagewisegroup.pagewise.Student

/**
 * A fragment representing a list of [Student] profiles.
 */
class ProfileViewFragment : Fragment() {

    private var columnCount = 1
    private val students = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //gets students from database
        val dbm = DatabaseManager(requireContext())
        val sCursor = dbm.readableDatabase?.rawQuery("SELECT * FROM STUDENTS", null)
        while (sCursor!!.moveToNext()) {
            students.add(dbm.fetchStudent(sCursor.getLong(0)))
        }
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        sCursor.close()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_view_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = ProfileRecyclerViewAdapter(students)
            }
        }
        return view
    }

    //Uses profile view activity to create new students/profiles
    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance() =
            ProfileViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, 1)
                }
            }
    }
}