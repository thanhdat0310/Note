package com.example.note.Interface

import java.text.FieldPosition

interface EventChange {
    fun changeData(title1: String, title2: String, position: Int)
    fun delete(position: Int)
}