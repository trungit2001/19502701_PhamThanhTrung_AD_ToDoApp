package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.getStatus

class TaskAdapter(
    private val toDoList: MutableList<ToDo>
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val desc: TextView = itemView.findViewById(R.id.tvTask)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.desc.text = toDoList[position].task!!.descTask
        val status: Boolean = toDoList[position].task!!.status

        holder.status.text = getStatus(status)
        holder.status.setTextColor(if (status) Color.GREEN else Color.RED)
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }
}