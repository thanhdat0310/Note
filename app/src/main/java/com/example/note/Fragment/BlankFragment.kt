package com.example.note.Fragment

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.note.R
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*


class BlankFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createNotificationChannel()
        submitBtn.setOnClickListener {
            showTimePicker()
        }
        btnAccept.setOnClickListener {
            setAlarm()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAlarm() {
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(activity!!.applicationContext, Notification::class.java)

        val  pendingIntent = PendingIntent.getBroadcast(activity!!.applicationContext, 0, intent, 0)
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        Toast.makeText(activity, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showTimePicker() {
        getDateTimeCalendar()
        DatePickerDialog(activity!!, this, year,month,day).show()
    }

    private fun getDateTimeCalendar() {
        val cal  = Calendar.getInstance()
        day =cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "foxandroidReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance)
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
        TimePickerDialog(activity,this,hour,minute,true).show()
    }

    override fun onTimeSet(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        saveHour = hourOfDay
        saveMinute = minute

    }
    private fun getTime(): Long {
        val minute = saveMinute
        val hour = saveHour
        val day = saveDay
        val month = saveMonth
        val year = saveYear

        val  calendar = Calendar.getInstance()
        calendar.set(year,month,day,hour,minute)
        return calendar.timeInMillis
    }

}