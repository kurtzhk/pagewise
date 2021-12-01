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
class StudentClassFragment : Fragment() {

    private var columnCount = 1
    lateinit var studentClasses: ArrayList<PWClass>

    val student = getContext()?.let { Student("Test student", 0.0, it,null) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        student?.createTempAssignments()
        student?.calculateReadingSpeed(null)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            //studentClasses = it.getParcelableArrayList<>()(STUDENT_CLASSES)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_class_view_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = student?.classes?.let { StudentClassRecyclerViewAdapter(it) }
            }
        }
        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        const val STUDENT_CLASSES = "student_class_list"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            StudentClassFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    //putParcelableArrayList(STUDENT_CLASSES, studentClasses)
                }
            }
    }
}