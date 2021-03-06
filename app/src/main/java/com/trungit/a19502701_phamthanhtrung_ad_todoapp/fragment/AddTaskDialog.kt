package com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R

class AddTaskDialog: DialogFragment() {
    private lateinit var listener: DialogAddItemListener
    interface DialogAddItemListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onDialogNeutralClick(dialog: DialogFragment)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.txtAddTask)
                .setView(R.layout.add_task_dialog)
                .setPositiveButton(
                    R.string.txtBtnSave
                ) { _, _ ->
                    listener.onDialogPositiveClick(this)
                }
                .setNegativeButton(
                    R.string.txtBtnCancel
                ) { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
                .setNeutralButton(
                    R.string.setDate
                ) { _, _ ->
                    listener.onDialogNeutralClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogAddItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}