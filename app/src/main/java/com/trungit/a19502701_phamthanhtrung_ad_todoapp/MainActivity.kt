package com.trungit.a19502701_phamthanhtrung_ad_todoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.AddTaskDialog
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.Task
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter.UpdateAndDelete
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.ToDo
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.toast

class MainActivity : AppCompatActivity(), AddTaskDialog.DialogAddItemListener, UpdateAndDelete {
    private lateinit var database: DatabaseReference
    private lateinit var rvTask: RecyclerView
    private lateinit var divider: RecyclerView.ItemDecoration
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var llManager: LinearLayoutManager
    private lateinit var toDoList: MutableList<ToDo>
    private val dbKey = "task"

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
        divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rvTask.layoutManager = llManager
        rvTask.addItemDecoration(divider)

        toDoList = ArrayList()

        findViewById<FloatingActionButton>(R.id.btnAddTask)
            .setOnClickListener {
                onClickBtnAddTask()
            }

        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoList.clear()
                getToDoList()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getToDoList() {
        database.child(dbKey)
            .get()
            .addOnSuccessListener {
                addItemToList(it)
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

        val itemUID = System.currentTimeMillis().toString()
        val txtDescTask = descTask.text.toString()
        if (txtDescTask != "") {
            val task = Task(txtDescTask, false)
            database
                .child(dbKey)
                .child(itemUID)
                .setValue(task)
                .addOnSuccessListener {
                    val newTask = ToDo(itemUID, task)
                    toDoList.add(newTask)
                    taskAdapter.notifyItemInserted(toDoList.size - 1)
                    toast(this, getString(R.string.txtSaveSuccess))
                }.addOnFailureListener {
                    toast(this, getString(R.string.txtSaveFailure))
                }
        } else {
            toast(this, getString(R.string.txtSaveEmpty))
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        toast(this, getString(R.string.txtCancel))
    }

    private fun addItemToList(snapshot : DataSnapshot){
        toDoList = snapshot.children.map { element ->
            ToDo(
                element.key.toString(),
                element.getValue(Task::class.java)
            )
        } as MutableList<ToDo>

        taskAdapter = TaskAdapter(this, toDoList)
        rvTask.adapter = taskAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = database.child(dbKey).child(itemUID)
        itemReference.child("status").setValue(isDone)
        taskAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child(dbKey).child(itemUID)
        itemReference.removeValue()
        taskAdapter.notifyDataSetChanged()
    }
}
