package com.trungit.a19502701_phamthanhtrung_ad_todoapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), AddTaskDialog.DialogAddItemListener {
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        database = Firebase.database.reference

        findViewById<FloatingActionButton>(R.id.btnAddTask).setOnClickListener {
            onClickBtnAddTask()
        }
    }

    private fun onClickBtnAddTask() {
        val addItemFragment = AddTaskDialog()
        addItemFragment.show(supportFragmentManager, "AddItemTag")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Toast.makeText(baseContext, "save", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(baseContext, "Cancel", Toast.LENGTH_SHORT).show()
    }
}