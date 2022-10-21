package com.example.note.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.Interface.Edit
import com.example.note.Interface.EventChange
import com.example.note.Interface.EventClick
import com.example.note.NoteAdapter
import com.example.note.NoteData
import com.example.note.R
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.FieldPosition

class MainFragment : Fragment(), EventClick, Edit, EventChange {

    private lateinit var noteList:ArrayList<NoteData>
    private lateinit var noteAdapter: NoteAdapter
    //private lateinit var edit: Edit


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteList = ArrayList()
        noteAdapter = NoteAdapter(noteList, this)

        recyclerview.layoutManager =LinearLayoutManager(context)
        recyclerview.adapter = noteAdapter

        btnAdd.setOnClickListener {

            Add()
        }

    }

    private fun Add(){
        val fragmentAdd = AddFragment()
        fragmentAdd.setEventClick(this)
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.framelayout, fragmentAdd)?.addToBackStack(null)?.commit()

    }

    override fun sendData(title: String, subtitle: String) {
        var message  = title
        val message1 = subtitle
        if(message.length==0 && message1.length>0) {
            noteList.add(NoteData("Không có tiêu đề", message1))
            noteAdapter.notifyDataSetChanged()
        }
        else if(message.length>0){
            noteList.add(NoteData(message, message1))
            noteAdapter.notifyDataSetChanged()
        }
    }

    override fun Edit(position: Int) {
        var fragmentedit = EditFragment.pass(noteList[position].title, noteList[position].sub_Title, position)
        fragmentedit.setEventChange(this)
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.framelayout, fragmentedit)?.addToBackStack(null)?.commit()
    }

    override fun changeData(title1: String, title2: String, position: Int) {
        var message  = title1
        val message1 = title2
        if(message.length==0 && message1.length>0) {

            noteList[position] = NoteData("Không có tiêu đề", message1)
            noteAdapter.notifyDataSetChanged()
        }
        else if(message.length>0){
            noteList[position] = NoteData(message, message1)
            noteAdapter.notifyDataSetChanged()
        }
    }

    override fun delete(position: Int) {
            noteList.removeAt(position)
            noteAdapter.notifyDataSetChanged()
    }


}