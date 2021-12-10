package com.trungit.a19502701_phamthanhtrung_ad_todoapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AddTaskDialog: DialogFragment() {
    private lateinit var addItemListener: DialogAddItemListener

    interface DialogAddItemListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.txtAddTask)
                .setView(R.layout.add_task_dialog)
                .setPositiveButton(R.string.txtSave
                ) { _, _ ->
                    addItemListener.onDialogPositiveClick(this)
                }
                .setNegativeButton(R.string.txtCancel
                ) { _, _ ->
                    addItemListener.onDialogNegativeClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            addItemListener = context as DialogAddItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    "must implement NoticeDialogListener"))
        }
    }
}