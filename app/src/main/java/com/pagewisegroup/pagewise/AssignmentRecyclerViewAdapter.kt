package com.pagewisegroup.pagewise

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

import com.pagewisegroup.pagewise.placeholder.PlaceholderContent.PlaceholderItem
import com.pagewisegroup.pagewise.databinding.FragmentAssignmentViewBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
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
        holder.contentView.text = assignment.dueDate.toString()
        holder.barView.progress = (assignment.progress.getPortionComplete() * 100).toInt()
        val progress = "${assignment.progress.getCurrentPage() - assignment.pageStart} / ${assignment.pageEnd - assignment.pageStart}"
        holder.progressTextView.text = progress
    }

    override fun getItemCount(): Int = assignments.size

    inner class ViewHolder(binding: FragmentAssignmentViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val barView : ProgressBar = binding.assignmentProgress
        val progressTextView : TextView = binding.progressText

        override fun toString(): String {
            return super.toString() + "'" + contentView.text + "'"
        }
    }
}