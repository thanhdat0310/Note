package com.example.note

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {


        val notification = NotificationCompat.Builder(context!!, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent!!.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = NotificationManagerCompat.from(context)
        manager.notify(notificationID, notification.build())
    }
}