package com.feissenger

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.feissenger.data.DataRepository
import com.feissenger.data.api.FCMApi
import com.feissenger.data.api.WebApi
import com.feissenger.data.api.model.ContactReadRequest
import com.feissenger.data.db.LocalCache
import com.feissenger.data.util.Injection
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.prefs.PreferenceChangeEvent


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = "admin_channel"
    private lateinit var dataRepository: DataRepository
    private lateinit var sharedPref: SharedPreferences
    fun CoroutineScope.go() = launch {
        dataRepository.loadMessages(
            {},
            ContactReadRequest(
                sharedPref.getString("uid", "")!!,
                sharedPref.getString("contactId", "")!!
            )
        )
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        dataRepository = Injection.provideDataRepository(applicationContext)
        sharedPref = getSharedPreferences(MainActivity::class.simpleName, Context.MODE_PRIVATE)

        runBlocking { go() }

        val activityManager =
            applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var isOpen = false
        for (appprocess in activityManager.runningAppProcesses) {
            if ((appprocess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appprocess.processName == "com.feissenger" && sharedPref.getString(
                    "fragment",
                    ""
                ) == "messages" && sharedPref.getString("contactId", "") == message.data.get("value")) || (sharedPref.getString("uid","") == "")
            ) {
                isOpen = !isOpen
                Log.i("Foreground App", appprocess.processName)
            }
        }


        if (!isOpen) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationID = Random().nextInt(3000)

            /*
            Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
            to at least one of them.
          */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels(notificationManager)
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            val extras = message.from?.split("/")?.last()?.split("_")
            intent.putExtra("typ", extras?.first())
            intent.putExtra("value", message.data["value"])

            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val largeIcon = BitmapFactory.decodeResource(
                resources,
                R.drawable.notification
            )


            val notificationSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(largeIcon)
                .setContentTitle(message.data["title"])
                .setContentText(message.data["message"])
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.color = resources.getColor(R.color.blue)
            }
            notificationManager.notify(notificationID, notificationBuilder.build())
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to devie notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }
}
