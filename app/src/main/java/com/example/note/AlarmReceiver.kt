package com.example.note

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val idNotice = "ID"
class AlarmReceiver : BroadcastReceiver() {



    override fun onReceive(context: Context, intent: Intent) {

        val title= intent.getStringExtra(titleExtra)
        val message = intent.getStringExtra(messageExtra)



        val builder = NotificationCompat.Builder(context, "thanhdat")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)


        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(123, builder.build())

    }


}