package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.getStatus

class TaskAdapter(
    private val context: Context,
    private val toDoList: MutableList<ToDo>
): BaseAdapter() {
    private val update = context as UpdateAndDelete

    interface UpdateAndDelete {
        fun modifyItem(itemUID: String, isDone: Boolean)
        fun onItemDelete(itemUID: String)
        fun editItem(itemUID: String, oldText: String)
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
        viewHolder.tvStatus.text = getStatus(context, status)
        viewHolder.tvStatus.setTextColor(
            if (status) getColor(context, R.color.green)
            else getColor(context, R.color.red)
        )

        viewHolder.descTask.setOnClickListener {
            update.editItem(itemUID, viewHolder.descTask.text.toString())
        }

        viewHolder.tvStatus.setOnClickListener {
            update.modifyItem(itemUID, !status)
        }

        viewHolder.btnDelete.setOnClickListener {
            update.onItemDelete(itemUID)
        }

        return view
    }

    class TaskViewHolder(itemView: View) {
        val descTask: TextView = itemView.findViewById(R.id.tvTask)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

}
