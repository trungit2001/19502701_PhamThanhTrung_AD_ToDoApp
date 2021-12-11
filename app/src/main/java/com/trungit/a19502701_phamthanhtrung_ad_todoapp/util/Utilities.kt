package com.trungit.a19502701_phamthanhtrung_ad_todoapp.util

import android.content.Context
import android.widget.Toast

object Utilities{
    fun toast(
        context: Context,
        res: String
    ) = Toast.makeText(context, res, Toast.LENGTH_SHORT).show()

    fun getStatus(
        status: Boolean
    ) = if (status) "Completed"
    else "Not Completed"
}