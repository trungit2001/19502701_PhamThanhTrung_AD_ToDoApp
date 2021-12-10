package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

interface UpdateAndDelete {
    fun modifyItem(itemUID: String, isDone: Boolean)
    fun onItemDelete(itemUID: String)
}