




package com.example.note.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.note.Interface.EventChange
import com.example.note.Interface.EventClick
import com.example.note.R
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.btnAccept
import kotlinx.android.synthetic.main.fragment_add.btnCancel
import kotlinx.android.synthetic.main.fragment_add.editText
import kotlinx.android.synthetic.main.fragment_add.subedittext
import kotlinx.android.synthetic.main.fragment_edit.*


class EditFragment : Fragment() {
    private lateinit var eventChange: EventChange
    fun setEventChange(eventChange: EventChange) {
        this.eventChange = eventChange
    }
    companion object{
        fun pass(message: String, message1: String, position:Int) : EditFragment {
            val bundle = Bundle()
            bundle.putString("ms1", message)
            bundle.putString("ms2", message1)
            bundle.putInt("position", position)

            val fragment = EditFragment()
            fragment.arguments = bundle
            return fragment
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val display1 = arguments?.getString("ms1")
        val display2 = arguments?.getString("ms2")

        editText.setText(display1)
        subedittext.setText(display2)
        btnCancel.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }


       btnAccept.setOnClickListener {
            val titleText = editText.text.toString()
            val subtitleText = subedittext.text.toString()
            val pst = arguments?.getInt("position")
           if (pst != null) {
               eventChange.changeData(titleText, subtitleText,pst)
           }
            activity?.supportFragmentManager?.popBackStack()
        }
        btnDelete.setOnClickListener {
            val pst = arguments?.getInt("position")
            if (pst != null) {
                eventChange.delete(pst)
            }
            activity?.supportFragmentManager?.popBackStack()
        }

    }


}