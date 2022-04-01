package com.pagewisegroup.pagewise.schedule

import android.icu.text.DateFormatSymbols
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.pagewisegroup.pagewise.databinding.FragmentScheduleViewBinding
import kotlin.math.ceil
import kotlin.math.floor

/**
 * [RecyclerView.Adapter] that can display a schedule made up of [PlannedDay].
 * this will take the student object's schedule and generate a view for it.
 */
class ScheduleRecyclerViewAdapter(private val schedule: ArrayList<PlannedDay>) : RecyclerView.Adapter<ScheduleRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentScheduleViewBinding.inflate(
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
        val reading = StringBuilder().append("${assignment.assignmentName} ${assignment.startPages}-${assignment.endPage} (")
        if(assignment.plannedMinutes > 60) reading.append("${floor(assignment.plannedMinutes/60).toInt()}h")
        val time = ceil(assignment.plannedMinutes%60).toInt()
        if (time > 0) reading.append(" ${time}min)")
        holder.contentView.text = reading
    }

    override fun getItemCount(): Int = schedule.size

    inner class ViewHolder(binding: FragmentScheduleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + "'" + contentView.text + "'"
        }
    }
}