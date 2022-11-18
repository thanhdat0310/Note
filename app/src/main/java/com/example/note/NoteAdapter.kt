package com.example.note

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.note.Interface.Edit
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.item.view.*

class NoteAdapter(val noteList: ArrayList<NoteData>, var edit: Edit): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.item,parent,false)
        return NoteViewHolder(v)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val newList = noteList[position]
        holder.title.text = newList.title
        holder.message.text = newList.message

        holder.itemView.setOnClickListener {

            edit.EditNote(position)

        }
        holder.btnDelete.setOnClickListener {
            edit.DeleteNote(position)
        }

    }

    override fun getItemCount(): Int {
        return noteList.size
    }



    inner class NoteViewHolder (val v : View) : RecyclerView.ViewHolder(v){
        var title : TextView
        var message :TextView
        var btnDelete : FloatingActionButton



        init {
            title = v.Title
            message = v.subTitle
            btnDelete = v.btnDelete
        }
    }

}