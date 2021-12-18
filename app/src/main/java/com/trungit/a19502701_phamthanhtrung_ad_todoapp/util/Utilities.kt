package com.trungit.a19502701_phamthanhtrung_ad_todoapp.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.trungit.a19502701_phamthanhtrung_ad_todoapp.R

object Utilities{
    fun toast(
        context: Context,
        res: String
    ) = Toast.makeText(context, res, Toast.LENGTH_SHORT).show()

    fun getStatus(
        context: Context,
        status: Boolean
    ) = if (status) context.getString(R.string.txtStatusCompleted)
    else context.getString(R.string.txtStatusNotCompleted)

    fun hideView(v: View) { v.visibility = View.INVISIBLE }
    fun showView(v: View) { v.visibility = View.VISIBLE }
}