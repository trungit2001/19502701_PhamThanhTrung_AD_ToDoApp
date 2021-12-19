package com.trungit.a19502701_phamthanhtrung_ad_todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.AddTaskDialog
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.AddTaskDialog.DialogAddItemListener
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.EditTaskDialog
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment.EditTaskDialog.DialogEditItemListener
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.Task
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.TaskAdapter.UpdateAndDelete
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.model.ToDo
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.onSnack
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.util.Utilities.toast
import java.util.*

class MainActivity: AppCompatActivity(),
    DialogAddItemListener,
    DialogEditItemListener,
    UpdateAndDelete {
    private lateinit var database: DatabaseReference
    private lateinit var lvTask: ListView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var toDoList: MutableList<ToDo>
    private lateinit var uid: String
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
        val descTask: EditText = dialog.dialog!!.findViewById(R.id.etAddTask)


        val txtDescTask = descTask.text.toString()
        if (txtDescTask != "") {
            val itemUID = System.currentTimeMillis().toString()

            // set default date time for deadline
            val cal = Calendar.getInstance()
            val dDef = cal.get(Calendar.DAY_OF_MONTH)
            val mDef = cal.get(Calendar.MONTH)
            val yDef = cal.get(Calendar.YEAR)
            val dateString =  String.format("%02d/%02d/%04d", dDef, mDef, yDef)

            val newTask = Task(txtDescTask, false, dateString)
            database
                .child(dbKey)
                .child(itemUID)
                .setValue(newTask)

            toast(this, getString(R.string.txtSaveSuccess))
        } else {
            toast(this, getString(R.string.txtSaveEmpty))
        }
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        toast(this, getString(R.string.txtCancel))
    }

    override fun onDialogNeutralClick(dialog: DialogFragment) {
        val descTask: EditText = dialog.dialog!!.findViewById(R.id.etAddTask)
        val txtDescTask = descTask.text.toString()
        if (txtDescTask != "") {
            val itemUID = System.currentTimeMillis().toString()
            pickDateAndAddNewTask(itemUID, txtDescTask)

        } else {
            toast(this, getString(R.string.txtSaveEmpty))
        }
    }

    private fun pickDateAndAddNewTask(
        itemUID: String,
        txtDescTask: String
    ) {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        DatePickerDialog(
            this@MainActivity, {
                    _, year, month, dayOfMonth ->
                if (dayOfMonth >= y && month >= m && year >= y) {
                    val dateString = String.format(
                        "%02d/%02d/%04d",
                        dayOfMonth,
                        month + 1,
                        year
                    )

                    val newTask = Task(txtDescTask, false, dateString)
                    database
                        .child(dbKey)
                        .child(itemUID)
                        .setValue(newTask)

                    toast(this@MainActivity, getString(R.string.txtSaveSuccess))
                } else {
                    onSnack(
                        this.findViewById(R.id.toDoListView),
                        getString(R.string.setTimeError)
                    )
                }
            }, y, m, d).show()
    }

    private fun addItemToList(snapshot : DataSnapshot){
        toDoList = snapshot
            .child("tasks")
            .children
            .map { element ->
            ToDo(
                element.key.toString(),
                element.getValue(Task::class.java)
            )
        } as MutableList<ToDo>

        taskAdapter = TaskAdapter(this, toDoList)
        lvTask.adapter = taskAdapter
    }

    override fun onStatusClick(itemUID: String, isDone: Boolean) {
        database
            .child(dbKey)
            .child(itemUID)
            .child("status")
            .setValue(isDone)
    }

    override fun onDeleteTaskClick(itemUID: String) {
        database
            .child(dbKey)
            .child(itemUID)
            .removeValue()
    }

    override fun onEditTaskClick(itemUID: String) {
        val dialog = EditTaskDialog()
        dialog.show(supportFragmentManager, "EditTask")

        this.uid = itemUID
    }

    override fun onDeadLineClick(itemUID: String) {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)

        DatePickerDialog(
            this@MainActivity, {
                    _, year, month, dayOfMonth ->
                if (dayOfMonth >= y && month >= m && year >= y) {
                    val dateString = String.format(
                        "%02d/%02d/%04d",
                        dayOfMonth,
                        month + 1,
                        year
                    )

                    database
                        .child(dbKey)
                        .child(itemUID)
                        .child("dateString")
                        .setValue(dateString)

                    toast(this@MainActivity, getString(R.string.txtSaveSuccess))
                } else {
                    onSnack(
                        this.findViewById(R.id.toDoListView),
                        getString(R.string.setTimeError)
                    )
                }

            }, y, m, d).show()
    }

    override fun posBtnClick(dialog: DialogFragment) {
        val txtEt = dialog
            .requireDialog()
            .findViewById<EditText>(R.id.etEditTask)
            .text

        if (txtEt.toString() != "") {
            database
                .child(dbKey)
                .child(uid)
                .child("descTask")
                .setValue(txtEt.toString())

            toast(this@MainActivity, getString(R.string.txtSaveSuccess))
        } else {
            toast(this@MainActivity, getString(R.string.txtSaveEmpty))
        }

    }

    override fun negBtnClick(dialog: DialogFragment) {
        toast(this@MainActivity, getString(R.string.txtCancel))
    }
}
