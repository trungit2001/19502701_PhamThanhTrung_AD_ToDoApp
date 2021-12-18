package com.trungit.a19502701_phamthanhtrung_ad_todoapp

import android.os.Bundle
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.AddTaskDialog
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.AddTaskDialog.DialogAddItemListener
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.Task
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter.UpdateAndDelete
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.ToDo
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.toast

class MainActivity : AppCompatActivity(), DialogAddItemListener, UpdateAndDelete {
    private lateinit var database: DatabaseReference
    private lateinit var lvTask: ListView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var toDoList: MutableList<ToDo>
    private val dbKey = "tasks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = Firebase.database.reference

        lvTask = findViewById(R.id.toDoListView)

        findViewById<FloatingActionButton>(R.id.btnAddTask)
            .setOnClickListener {
                showDialogAddTask()
            }

        toDoList = mutableListOf()
        taskAdapter = TaskAdapter(this, toDoList)
        lvTask.adapter = taskAdapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toDoList.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                toast(this@MainActivity, getString(R.string.txtGetToDoListFailure))
            }
        })
    }

    private fun showDialogAddTask() {
        val dialog = AddTaskDialog()
        dialog.show(supportFragmentManager, "AddItemTag")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        val descTask: EditText = dialog.dialog!!.findViewById(R.id.etTask)

        val itemUID = System.currentTimeMillis().toString()
        val txtDescTask = descTask.text.toString()
        if (txtDescTask != "") {
            val task = Task(txtDescTask, false)
            database.child(dbKey).child(itemUID).setValue(task)
            toast(this, getString(R.string.txtSaveSuccess))
        } else {
            toast(this, getString(R.string.txtSaveEmpty))
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        toast(this, getString(R.string.txtCancel))
    }

    private fun addItemToList(snapshot : DataSnapshot){
        toDoList = snapshot.child("tasks").children.map { element ->
            ToDo(
                element.key.toString(),
                element.getValue(Task::class.java)
            )
        } as MutableList<ToDo>

        taskAdapter = TaskAdapter(this, toDoList)
        lvTask.adapter = taskAdapter
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = database.child(dbKey).child(itemUID)
        itemReference.child("status").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child(dbKey).child(itemUID)
        itemReference.removeValue()
        taskAdapter.notifyDataSetChanged()
    }

    override fun editItem(itemUID: String, oldText: String) {
        val itemReference = database.child(dbKey).child(itemUID)
        val alertDialog = AlertDialog.Builder(this)
        val txtEt = EditText(this)

        alertDialog.setMessage("Current task: $oldText")
        alertDialog.setTitle(getString(R.string.txtEditTask))
        alertDialog.setView(txtEt)

        alertDialog.setPositiveButton(getString(R.string.txtBtnSave)){
                dialog, _ ->
            itemReference.child("descTask").setValue(txtEt.text.toString())
            dialog.dismiss()
            toast(this, getString(R.string.txtSaveSuccess))
        }

        alertDialog.setNegativeButton(getText(R.string.txtBtnCancel)){
                _, _ ->
            toast(this, getString(R.string.txtSaveFailure))
        }

        alertDialog.show()
    }
}
