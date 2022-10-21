package com.example.note.Fragment

import android.annotation.SuppressLint
import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.note.*
import com.example.note.Interface.EventClick
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.item.*
import java.util.*

class AddFragment : Fragment(),DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    var saveHour = 0
    var saveMinute = 0

    var time = Long

    lateinit var eventclick: EventClick

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_add, container, false)
        return view

    }
    fun setEventClick(eventClick: EventClick) {
        this.eventclick = eventClick
    }
    @SuppressLint("UseRequireInsteadOfGet")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()
        btnsettime.setOnClickListener {
            showTimePicker()

        }
        btnCancel.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        btnAccept.setOnClickListener {
            add()
            setAlarm()
        }

    }
    private fun add(){
        val titleText = editText.text.toString()
        val subtitleText = subedittext.text.toString()
        eventclick.sendData(titleText, subtitleText)
        activity?.supportFragmentManager?.popBackStack()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAlarm() {
        val intent = Intent(activity!!.baseContext , AlarmReceiver::class.java)
        val title = editText.text.toString()
        val message = subedittext.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra,message)
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val  pendingIntent = PendingIntent.getBroadcast(activity!!.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

       // Toast.makeText(activity, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }
    private fun getDateTimeCalendar(){
        val cal  = java.util.Calendar.getInstance()
        day =cal.get(java.util.Calendar.DAY_OF_MONTH)
        month = cal.get(java.util.Calendar.MONTH)
        year = cal.get(java.util.Calendar.YEAR)
        hour = cal.get(java.util.Calendar.HOUR)
        minute = cal.get(java.util.Calendar.MINUTE)
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun showTimePicker() {
        getDateTimeCalendar()
        DatePickerDialog(activity!!, this, year,month,day).show()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "ReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("thanhdat", name, importance)
            channel.description = description
            val notificationManager: NotificationManager =
                requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveDay = dayOfMonth
        saveMonth = month
        saveYear = year

        getDateTimeCalendar()
        TimePickerDialog(activity,this,hour,minute,false).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour = hourOfDay
        saveMinute = minute
       // setAlarm()

    }
    private fun getTime(): Long {
        val minute = saveMinute
        val hour = saveHour
        val day = saveDay
        val month = saveMonth
        val year = saveYear

        val  calendar = java.util.Calendar.getInstance()
        calendar.set(year,month,day,hour,minute)
        return calendar.timeInMillis
    }


}