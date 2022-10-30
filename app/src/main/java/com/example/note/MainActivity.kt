package com.example.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.note.Fragment.AddFragment
import com.example.note.Fragment.MainFragment
import com.example.note.Interface.Communicator

class MainActivity : AppCompatActivity() , Communicator{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = MainFragment()
        supportFragmentManager.beginTransaction().add(R.id.framelayout, fragment).commit()
    }

    override fun passData(idAlarm: Int) {
        val bundle = Bundle()
        bundle.putInt("dat1348", idAlarm)
        val fragmentb = MainFragment()
        fragmentb.arguments = bundle
    }
}