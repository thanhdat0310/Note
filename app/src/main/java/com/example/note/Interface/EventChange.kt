package com.example.note.Interface

import java.text.FieldPosition

interface EventChange {
    fun changeData(title: String, message: String, position: Int)

}