package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.getStatus

class TaskAdapter(
    private val context: Context,
    private val toDoList: MutableList<ToDo>
): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var rowIndex = -1

    interface UpdateAndDelete {
        fun modifyItem(itemUID: String, isDone: Boolean)
        fun onItemDelete(itemUID: String)
    }

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val desc: TextView = itemView.findViewById(R.id.tvTask)
        val status: TextView = itemView.findViewById(R.id.tvStatus)
        val taskItemLayout: LinearLayout = itemView.findViewById(R.id.taskItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TaskViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.desc.text = toDoList[position].task!!.descTask
        val status: Boolean = toDoList[position].task!!.status

        holder.status.text = getStatus(status)
        holder.status.setTextColor(
            if (status) getColor(context, R.color.green)
            else getColor(context, R.color.red)
        )

        if (rowIndex == position) {
            holder.taskItemLayout.setBackgroundColor(
                getColor(context, R.color.purple_200)
            )
            holder.desc.setTextColor(
                getColor(context, R.color.white)
            )
        } else {
            holder.taskItemLayout.setBackgroundColor(
                getColor(context, R.color.white)
            )
            holder.desc.setTextColor(
                getColor(context, R.color.black)
            )
        }

        holder.taskItemLayout.setOnClickListener {
            rowIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }


}