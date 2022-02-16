package com.pagewisegroup.pagewise

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.pagewisegroup.pagewise.databinding.FragmentClassViewBinding

/**
 * [RecyclerView.Adapter] that can display a [PWClass].
 * this will take the student object's PWClass and generate a view for it.
 */
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
        holder.timeView.text = "Spring 2020"
        holder.pwClass = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentClassViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val nameView: TextView = binding.className
        val timeView: TextView = binding.classTime
        lateinit var pwClass: PWClass

        init {
            itemView.setOnClickListener(this)
        }

        override fun toString(): String {
            return super.toString() + " '" + timeView.text + "'"
        }

        override fun onClick(v: View) {
            if (v.context is StudentViewActivity) {
                (v.context as StudentViewActivity).displayAssignmentView(pwClass.assignments)
            }
        }
    }

}