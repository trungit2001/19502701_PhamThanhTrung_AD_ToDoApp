package com.trungit.a19502701_phamthanhtrung_ad_todoapp.model

class TodoModel {
    var uid: String = ""
    var itemDataText: String = ""
    var isDone: Boolean = false

    companion object Factory {
        fun createList(): TodoModel = TodoModel()
    }
}