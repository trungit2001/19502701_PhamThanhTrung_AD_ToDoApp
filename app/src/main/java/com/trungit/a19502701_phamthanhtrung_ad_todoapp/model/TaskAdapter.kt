package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.MainActivity
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.getStatus
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.onSnack

class TaskAdapter(
    private val mainActivity: MainActivity,
    private val toDoList: MutableList<ToDo>
): BaseAdapter(){
    private val update = mainActivity as UpdateAndDelete

    interface UpdateAndDelete {
        fun onStatusClick(itemUID: String, isDone: Boolean)
        fun onDeleteTaskClick(itemUID: String)
        fun onEditTaskClick(itemUID: String)
        fun onDeadLineClick(itemUID: String)
    }

    override fun getCount(): Int {
        return toDoList.size
    }

    override fun getItem(position: Int): Any {
        return toDoList[position]
    }

    override fun getItemId(position: Int): Long {
        return toDoList[position].itemUID!!.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemUID = toDoList[position].itemUID!!
        val descTask = toDoList[position].task!!.descTask
        val status = toDoList[position].task!!.status
        val dateString = toDoList[position].task!!.dateString
        val view: View
        val viewHolder: TaskViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(parent!!.context).inflate(
                R.layout.item_todo,
                parent,
                false)
            viewHolder = TaskViewHolder(view)
            view.tag = viewHolder
        }else {
            view = convertView
            viewHolder = view.tag as TaskViewHolder
        }

        // set text for list view to do
        viewHolder.descTask.text = descTask
        viewHolder.tvDate.text = dateString
        viewHolder.tvStatus.text = getStatus(mainActivity, status)
        viewHolder.tvStatus.setTextColor(
            if (status) getColor(mainActivity, R.color.green)
            else getColor(mainActivity, R.color.red)
        )

        viewHolder.descTask.setOnClickListener {
            update.onEditTaskClick(itemUID)
        }

        viewHolder.tvStatus.setOnClickListener {
            update.onStatusClick(itemUID, !status)
            if (!status) {
                onSnack(it, mainActivity.getString(R.string.markCompleted))
            } else {
                onSnack(it, mainActivity.getString(R.string.markNotCompleted))
            }
        }

        viewHolder.tvDate.setOnClickListener {
            update.onDeadLineClick(itemUID)
        }

        viewHolder.btnDelete.setOnClickListener {
            update.onDeleteTaskClick(itemUID)
        }

        return view
    }

    class TaskViewHolder(itemView: View) {
        val descTask: TextView = itemView.findViewById(R.id.tvTask)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }
}
