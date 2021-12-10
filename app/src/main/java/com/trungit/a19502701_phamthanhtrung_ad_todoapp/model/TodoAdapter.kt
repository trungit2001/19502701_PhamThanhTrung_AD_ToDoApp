package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R

class TodoAdapter(context: Context, todoList: MutableList<TodoModel>): BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val itemList = todoList
    private val updateAndDelete: UpdateAndDelete = context as UpdateAndDelete

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val uid : String  = itemList[position].uid
        val itemDataText = itemList[position].itemDataText
        val isDone : Boolean = itemList[position].isDone

        val view: View
        val viewHolder: ListViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.add_task_dialog, parent, false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ListViewHolder
        }

        viewHolder.tvTask.text = itemDataText
        viewHolder.isDone.setOnClickListener {
            viewHolder.tvStatus.text = R.string.txtStatusCompleted.toString()
            viewHolder.tvStatus.setTextColor(Color.GREEN)
            updateAndDelete.modifyItem(uid, !isDone)
        }

        viewHolder.btnDelete.setOnClickListener {
            Log.d("del", "Deleted!")
            updateAndDelete.onItemDelete(uid)
        }

        return view
    }

    private class ListViewHolder(row: View?) {
        val tvTask: TextView = row!!.findViewById(R.id.tvTask)
        val tvStatus: TextView = row!!.findViewById(R.id.tvStatus)
        val btnEdit: Button = row!!.findViewById(R.id.btnEdit)
        val btnDelete: Button = row!!.findViewById(R.id.btnDelete)
        val isDone: Button = row!!.findViewById(R.id.btnCheck)
    }
}