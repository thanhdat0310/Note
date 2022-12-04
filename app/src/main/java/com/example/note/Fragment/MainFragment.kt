package com.example.note.Fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.note.*
import com.example.note.Interface.Edit
import com.example.note.Interface.EventChange
import com.example.note.Interface.EventClick
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

    override fun sendData(title: String, message: String) {
        var title1  = title
        val message1 = message
        if(title1.length==0 && message1.length>0) {
            noteList.add(NoteData("Không có tiêu đề", message1))
            noteAdapter.notifyDataSetChanged()
        }
        else if(title1.length>0){
            noteList.add(NoteData(title1, message1))
            noteAdapter.notifyDataSetChanged()

        }
    }
    var i=0
    @SuppressLint("UseRequireInsteadOfGet")
    fun GetIdNotice(): Int{  // nhận Id notice được gửi từ AddFragment
        requireActivity().supportFragmentManager.setFragmentResultListener("thanhdat", this, FragmentResultListener { requestKey, result ->
            val idnotice = result.getInt("dat")
            i = idnotice
        } )
        return i
    }

    override fun EditNote(position: Int) {
        var idnotice = GetIdNotice()
        var fragmentedit = EditFragment.pass(noteList[position].title, noteList[position].message, position, idnotice) // gửi Id notice sang edit fragment

        fragmentedit.setEventChange(this)
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.framelayout, fragmentedit)?.addToBackStack(null)?.commit()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun DeleteNote(position: Int) {

        val idN= GetIdNotice()
        val intent = Intent(activity , AlarmReceiver::class.java)
        val  pendingIntent = PendingIntent.getBroadcast(
            requireActivity().applicationContext,
            idN,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent) // hủy thông báo

        noteList.removeAt(position) // xóa item
        noteAdapter.notifyDataSetChanged()
    }

    override fun changeData(title: String, message: String, position: Int) {
        var title1  = title
        val message1 = message
        if(title1.length==0 && message1.length>0) {

            noteList[position] = NoteData("Không có tiêu đề", message1)
            noteAdapter.notifyDataSetChanged()
        }
        else if(title1.length>0){
            noteList[position] = NoteData(title1, message1)
            noteAdapter.notifyDataSetChanged()
        }
    }





}