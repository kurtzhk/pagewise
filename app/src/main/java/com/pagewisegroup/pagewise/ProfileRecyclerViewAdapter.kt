package com.pagewisegroup.pagewise

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.pagewisegroup.pagewise.placeholder.PlaceholderContent.PlaceholderItem
import com.pagewisegroup.pagewise.databinding.FragmentProfileViewBinding

/**
 * [RecyclerView.Adapter] that can display a [Profile].
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
        holder.idView.text = "ID: " + item.id.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentProfileViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.profileName
        val idView: TextView = binding.profileId

        override fun toString(): String {
            return super.toString() + " '" + nameView.text + "," + idView.text + "'"
        }
    }

}