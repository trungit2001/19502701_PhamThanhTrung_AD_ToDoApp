package com.trungit.a19502701_phamthanhtrung_ad_todoapp

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.AddTaskDialog
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.Task
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.ToDo
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.toast

class MainActivity : AppCompatActivity(), AddTaskDialog.DialogAddItemListener {
    private lateinit var database: DatabaseReference
    private lateinit var rvTask: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var llManager: LinearLayoutManager
    private lateinit var toDoList: MutableList<ToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.reference

        rvTask = findViewById(R.id.toDoListRecycler)
        llManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        rvTask.layoutManager = llManager

        toDoList = ArrayList()
        getToDoList()

        findViewById<FloatingActionButton>(R.id.btnAddTask)
            .setOnClickListener {
                onClickBtnAddTask()
            }
    }

    private fun getToDoList() {
        database.child("tasks")
            .get()
            .addOnSuccessListener {
                toDoList = it.children.map { element ->
                    ToDo(
                        element.key.toString(),
                        element.getValue(Task::class.java)
                    )
                } as MutableList<ToDo>

                Log.d("res", toDoList.toString())

                taskAdapter = TaskAdapter(toDoList)
                rvTask.adapter = taskAdapter
            }
            .addOnFailureListener{
                toast(this, getString(R.string.txtGetToDoListFailure))
            }
    }

    private fun onClickBtnAddTask() {
        val addItemFragment = AddTaskDialog()
        addItemFragment.show(supportFragmentManager, "AddItemTag")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val descTask: EditText = dialog.dialog!!.findViewById(R.id.etAddTask)

        val uid = System.currentTimeMillis().toString()
        val task = Task(descTask.text.toString(), false)
        database
            .child("tasks")
            .child(uid)
            .setValue(task)
            .addOnSuccessListener {
                val newTask = ToDo(uid, task)
                toDoList.add(newTask)
                taskAdapter.notifyItemInserted(toDoList.size - 1)
                toast(this, getString(R.string.txtSaveSuccess))
            }.addOnFailureListener {
                toast(this, getString(R.string.txtSaveFailure))
            }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        toast(this, getString(R.string.txtCancel))
    }
}
