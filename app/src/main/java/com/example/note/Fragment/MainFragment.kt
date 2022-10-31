package com.example.note.Fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.AlarmReceiver
import com.example.note.Interface.Edit
import com.example.note.Interface.EventChange
import com.example.note.Interface.EventClick
import com.example.note.NoteAdapter
import com.example.note.NoteData
import com.example.note.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), EventClick, Edit, EventChange {

    private lateinit var noteList:ArrayList<NoteData>
    private lateinit var noteAdapter: NoteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
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
    var i=0
    @SuppressLint("UseRequireInsteadOfGet")
    fun nhandulieu(): Int{

        requireActivity().supportFragmentManager.setFragmentResultListener("thanhdat", this, FragmentResultListener { requestKey, result ->
            val idnotice = result.getInt("dat")
            i = idnotice
        } )
        return i

    }

    override fun EditNote(position: Int) {
        var idnotice = nhandulieu()
        Log.d("test123", idnotice.toString())
        var fragmentedit = EditFragment.pass(noteList[position].title, noteList[position].sub_Title, position, idnotice)

        fragmentedit.setEventChange(this)
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.framelayout, fragmentedit)?.addToBackStack(null)?.commit()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun DeleteNote(position: Int) {

        val idN= nhandulieu()
        val intent = Intent(activity , AlarmReceiver::class.java)
        val  pendingIntent = PendingIntent.getBroadcast(
            requireActivity().applicationContext,
            idN,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)

        noteList.removeAt(position)
        noteAdapter.notifyDataSetChanged()
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




}