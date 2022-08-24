package com.example.life_manager

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.life_manager.WorkActivity

public class MemoBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val repeating_Intent = Intent(context, WorkActivity::class.java)
        repeating_Intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            repeating_Intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, "Notification")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.resources.getString(R.string.notification_title))
                .setContentText(context.resources.getString(R.string.notification_message))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(200, builder.build())
    }
}