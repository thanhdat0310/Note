




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
import androidx.annotation.RequiresApi
import com.example.note.AlarmReceiver
import com.example.note.Interface.EventChange
import com.example.note.Interface.EventClick
import com.example.note.R
import com.example.note.messageExtra
import com.example.note.titleExtra
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.btnAccept
import kotlinx.android.synthetic.main.fragment_add.btnCancel
import kotlinx.android.synthetic.main.fragment_add.editText
import kotlinx.android.synthetic.main.fragment_add.subedittext
import kotlinx.android.synthetic.main.fragment_edit.*


class EditFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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
        createNotificationChannel()
        val display1 = arguments?.getString("ms1")
        val display2 = arguments?.getString("ms2")

        editText.setText(display1)
        subedittext.setText(display2)
        btnHuy.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        btnSua.setOnClickListener {
            val titleText = editText.text.toString()
            val subtitleText = subedittext.text.toString()
            val pst = arguments?.getInt("position")
           if (pst != null) {
               eventChange.changeData(titleText, subtitleText,pst)
           }
            activity?.supportFragmentManager?.popBackStack()
        }
        /*btnXoa.setOnClickListener {
            cancelAlarm()
            val pst = arguments?.getInt("position")
            if (pst != null) {
                eventChange.delete(pst)
            }
            activity?.supportFragmentManager?.popBackStack()
        }*/

        btnsettime1.setOnClickListener {
            showTimePicker()
        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun showTimePicker() {
        getDateTimeCalendar()
        DatePickerDialog(activity!!, this, year,month,day).show()
    }

    private fun getDateTimeCalendar() {
        val cal  = java.util.Calendar.getInstance()
        day =cal.get(java.util.Calendar.DAY_OF_MONTH)
        month = cal.get(java.util.Calendar.MONTH)
        year = cal.get(java.util.Calendar.YEAR)
        hour = cal.get(java.util.Calendar.HOUR)
        minute = cal.get(java.util.Calendar.MINUTE)
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
        setAlarm()

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
    @SuppressLint("UseRequireInsteadOfGet")
    private fun cancelAlarm(){
        val intent = Intent(activity!!.baseContext , AlarmReceiver::class.java)
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val  pendingIntent = PendingIntent.getBroadcast(activity!!.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
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

}