package com.example.note.Interface

import com.example.note.NoteData

interface Edit {
    fun EditNote(position: Int)
    fun DeleteNote(position: Int)
}