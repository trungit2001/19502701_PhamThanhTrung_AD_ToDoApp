package com.trungit.a19502701_phamthanhtrung_ad_todoapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R

class EditTaskDialog: DialogFragment(){
    private lateinit var listener: DialogEditItemListener

    interface DialogEditItemListener {
        fun posBtnClick(dialog: DialogFragment)
        fun negBtnClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder
                .setTitle(R.string.txtEditTask)
                .setView(R.layout.edit_task_dialog)
                .setPositiveButton(
                    R.string.txtBtnSave
                ) { _, _ ->
                    listener.posBtnClick(this)
                }
                .setNegativeButton(
                    R.string.txtBtnCancel
                ) { _, _ ->
                    listener.negBtnClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DialogEditItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }
}