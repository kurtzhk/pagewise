package com.pagewisegroup.pagewise.profile

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pagewisegroup.pagewise.Student
import com.pagewisegroup.pagewise.StudentViewActivity

import com.pagewisegroup.pagewise.databinding.FragmentProfileViewBinding

/**
 * [RecyclerView.Adapter] that can display a [Student].
 * TODO: Replace the implementation with code for your data type.
 */
class ProfileRecyclerViewAdapter(
    private val values: List<Student>
) : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentProfileViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.nameView.text = item.name
        holder.idView.text = "ID: ${item.id.toString()}"
        holder.student = item
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentProfileViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val nameView: TextView = binding.profileName
        val idView: TextView = binding.profileId
        lateinit var student: Student
        init {
            itemView.setOnClickListener(this)
        }

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "," + idView.text + "'"
        }

        override fun onClick(v: View) {
            val intent = Intent(nameView.context, StudentViewActivity::class.java)
            intent.putExtra("STUDENT", student)
            v.context.startActivity(intent)
        }
    }

}