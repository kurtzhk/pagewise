package com.pagewisegroup.pagewise

import android.icu.text.DateFormatSymbols
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.pagewisegroup.pagewise.databinding.FragmentAssignmentViewBinding
import com.pagewisegroup.pagewise.placeholder.PlaceholderContent.PlaceholderItem
import kotlin.math.ceil
import kotlin.math.floor

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ScheduleRecyclerViewAdapter(private val schedule: ArrayList<PlannedDay>) : RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder>() {
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
        val day = schedule[position]
        //date
        val date = StringBuilder().append(DateFormatSymbols().months[day.date.month]).append(" ").append(day.date.date)
        holder.idView.text = date
        //reading
        val assignment = day.reading[0]
        val reading = StringBuilder().append(assignment.assignmentName).append(" ").append(assignment.startPages)
            .append("-").append(assignment.endPage).append(" (")
        if(assignment.plannedMinutes > 60) {
            reading.append(floor(assignment.plannedMinutes/60).toInt())
                .append("hrs ")
                .append(ceil(assignment.plannedMinutes%60).toInt())
        } else {
            reading.append(floor(assignment.plannedMinutes).toInt())
        }
        reading.append("mins)")
        holder.contentView.text = reading
    }

    override fun getItemCount(): Int = schedule.size

    inner class ViewHolder(binding: FragmentAssignmentViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + "'" + contentView.text + "'"
        }
    }
}