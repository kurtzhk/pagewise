package com.pagewisegroup.pagewise

import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi

import com.pagewisegroup.pagewise.placeholder.PlaceholderContent.PlaceholderItem
import com.pagewisegroup.pagewise.databinding.FragmentAssignmentViewBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ScheduleRecyclerViewAdapter(private val SchedulePlanner: SchedulePlanner) : RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentAssignmentViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = SchedulePlanner.schedule[position]
        holder.idView.text = position.toString()
        holder.contentView.text = schedule.toString()
    }

    override fun getItemCount(): Int = SchedulePlanner.schedule.size

    inner class ViewHolder(binding: FragmentAssignmentViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + "'" + contentView.text + "'"
        }
    }
}