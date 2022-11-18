package com.example.note.Fragment

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.note.*
import com.example.note.Interface.EventClick
import kotlinx.android.synthetic.main.fragment_add.*
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
            val titleText = edittextTitle.text.toString()
            val messageText = edittextMessage.text.toString()

            if (titleText.length==0 && messageText.length==0) {    //nếu chưa nhập thông tin thì hiển thị thông báo
                val builder1: AlertDialog.Builder = AlertDialog.Builder(activity)
                builder1.setTitle("LƯU Ý")
                builder1.setMessage("Bạn cần nhập thông tin !!!")
                builder1.setCancelable(true)
                builder1.setNeutralButton(
                    android.R.string.ok
                ) { dialog, id -> dialog.cancel() }
                val alert11 = builder1.create()
                alert11.show()
            }
            else{
            add()
            setAlarm()
            }
        }

    }
    private fun add(){
        val title = edittextTitle.text.toString()
        val message = edittextMessage.text.toString()
        eventclick.sendData(title, message)
        activity?.supportFragmentManager?.popBackStack()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAlarm() {

        val intent = Intent(activity!!.baseContext , AlarmReceiver::class.java)
        val title = edittextTitle.text.toString()
        val message = edittextMessage.text.toString()
        val uniqueID = System.currentTimeMillis()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra,message)
        intent.putExtra(idNotice,uniqueID)
        val result = Bundle()
        val notifyIdLong = uniqueID/1000
        var notifyIdInteger = notifyIdLong.toInt()

        result.putInt("dat", notifyIdInteger) // thấy thời gian hiện tại làm id cho alarm
        requireActivity().supportFragmentManager.setFragmentResult("thanhdat", result)
        val time1 = gettimecurrent()
        val  pendingIntent = PendingIntent.getBroadcast(activity!!.applicationContext, notifyIdInteger, intent,PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        Log.d("timehientai2", time.toString())
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        if (time1/100000000000 == time/100000000000){  // so sánh thời gian hiện tại và thời gian báo thức : nếu giống nhau thì hủy báo thức
            alarmManager.cancel(pendingIntent)
        }
       // Toast.makeText(activity, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }
    private fun getDateTimeCalendar(){  // lấy thời gian hiện tại để hiện thị trên datePickerDialog
        val cal  =Calendar.getInstance()
        day =cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
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
        TimePickerDialog(activity,this,hour,minute,true).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
    private fun gettimecurrent():Long{  // lấy thời gian hiện tại

        val day =Calendar.DAY_OF_MONTH
        val month =Calendar.MONTH
        val year = Calendar.YEAR
        val hour = Calendar.HOUR
        val minute = Calendar.MINUTE
        val cal  =Calendar.getInstance()

        cal.set(year,month,day,hour,minute)
        return cal.timeInMillis
    }


}