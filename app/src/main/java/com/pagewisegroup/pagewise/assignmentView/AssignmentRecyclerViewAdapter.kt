package com.pagewisegroup.pagewise.assignmentView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pagewisegroup.pagewise.Assignment
import com.pagewisegroup.pagewise.databinding.FragmentAssignmentViewBinding
import kotlin.math.ceil
import kotlin.math.floor

/**
 * [RecyclerView.Adapter] that can display a [Assignment] list.
 * this will take the student object's [Assignment] and generate a view for it.
 */
class AssignmentRecyclerViewAdapter(private val assignments: ArrayList<Assignment>) : RecyclerView.Adapter<AssignmentRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentAssignmentViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val assignment = assignments[position]
        holder.idView.text = assignment.name
        //writes date
        val yr = (assignment.dueDate.year+1900).toString()
        holder.contentView.text = "${assignment.dueDate.month}/${assignment.dueDate.date}/${yr.subSequence(yr.length-2, yr.length)}"
        //calculates progress
        holder.barView.progress = (assignment.progress.getPortionComplete() * 100).toInt()
        val progress = "${assignment.progress.getCurrentPage() - assignment.pageStart}/${assignment.pageEnd - assignment.pageStart}"
        holder.progressTextView.text = progress
        val time = StringBuilder()
        //writes and rounds estimated time
        if(assignment.timeToComplete > 60) time.append("${floor(assignment.timeToComplete).toInt()/60}h")
        val min = ceil(assignment.timeToComplete%60).toInt()
        if(min > 0) time.append(" ${min}min")
        holder.timeTextView.text = time
    }

    //Number of assignments
    override fun getItemCount(): Int = assignments.size

    inner class ViewHolder(binding: FragmentAssignmentViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val barView : ProgressBar = binding.assignmentProgress
        val progressTextView : TextView = binding.progressText
        val timeTextView : TextView = binding.timeLeft

        override fun toString(): String {
            return super.toString() + "'" + contentView.text + "'"
        }
    }
}