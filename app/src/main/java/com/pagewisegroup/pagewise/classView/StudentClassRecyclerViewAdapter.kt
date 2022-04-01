package com.pagewisegroup.pagewise.classView

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.pagewisegroup.pagewise.Assignment
import com.pagewisegroup.pagewise.PWClass
import com.pagewisegroup.pagewise.StudentViewActivity

import com.pagewisegroup.pagewise.databinding.FragmentClassViewBinding
import java.lang.StringBuilder
import java.lang.System.currentTimeMillis
import kotlin.math.ceil
import kotlin.math.floor

/**
 * [RecyclerView.Adapter] that can display a [PWClass] list.
 * this will take the student object's [PWClass] and generate a view for it.
 */

const val WEEK_IN_MILLIS : Long = 604800000000

class StudentClassRecyclerViewAdapter(private val values: List<PWClass>) : RecyclerView.Adapter<StudentClassRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentClassViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameView.text = item.name
        val curTime : Long = currentTimeMillis()
        //writes and calculates progress
        val weekAssignments : List<Assignment> = item.assignments.filter { (it.dueDate.time >= curTime && (it.dueDate.time <= curTime + WEEK_IN_MILLIS)) }
        val assignmentProgresses : Float = weekAssignments.map { it.progress.getCurrentPage() }.sum().toFloat() + 1
        val assignmentPages : Float = weekAssignments.map { it.pageEnd }.sum().toFloat() + 1
        val timeLeft = weekAssignments.sumOf{it.timeToComplete}
        holder.bar.progress = ((assignmentProgresses / assignmentPages) * 100).toInt()
        holder.pwClass = item
        val progressText = "Weekly Progress: ${holder.bar.progress}%"
        holder.progressText.text = progressText
        //writes and calculates time
        val time = StringBuilder()
        if(timeLeft > 60) time.append("${floor(timeLeft).toInt()/60}h")
        val min = ceil(timeLeft%60).toInt()
        if(min > 0) time.append(" ${min}min")
        if(timeLeft > 0) time.append(" left")
        else time.append("Finished")
        holder.timeView.text = time
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentClassViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val nameView: TextView = binding.className
        val bar: ProgressBar = binding.weeklyProgress
        val progressText: TextView = binding.progressText
        val timeView: TextView = binding.classTime
        lateinit var pwClass: PWClass

        init {
            itemView.setOnClickListener(this)
        }

        override fun toString(): String {
            return super.toString() + " '" //+ timeView.text + "'"
        }

        //when clicked display assignment view for that assignment
        override fun onClick(v: View) {
            if (v.context is StudentViewActivity) {
                (v.context as StudentViewActivity).displayAssignmentView(pwClass.assignments)
            }
        }
    }

}