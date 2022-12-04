package com.example.note

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val idNotice = "ID"
open class AlarmReceiver : BroadcastReceiver() {




    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(context,0,i,0)

        val title= intent.getStringExtra(titleExtra)
        val message = intent.getStringExtra(messageExtra)
        val uniqueID = System.currentTimeMillis()

        val builder = NotificationCompat.Builder(context, "thanhdat")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setStyle( NotificationCompat.BigTextStyle().bigText(message))

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(uniqueID.toInt(), builder.build())





    }



}