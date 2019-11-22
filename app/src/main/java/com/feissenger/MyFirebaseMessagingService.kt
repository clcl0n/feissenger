package com.feissenger

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.feissenger.data.DataRepository
import com.feissenger.data.api.WebApi
import com.feissenger.data.api.model.ContactReadRequest
import com.feissenger.data.api.model.RegisterTokenRequest
import com.feissenger.data.api.model.RoomReadRequest
import com.feissenger.data.util.Injection
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = "admin_channel"
    private lateinit var dataRepository: DataRepository
    private lateinit var sharedPref: MySharedPreferences


    private fun CoroutineScope.go() = launch {
        dataRepository.loadMessages(
            {},
            ContactReadRequest(
                sharedPref.get("uid") as String,
                sharedPref.get("contactId") as String
            )
        )

        dataRepository.loadRoomMessages(
            {},
            RoomReadRequest(sharedPref.get("uid") as String, sharedPref.get("activeWifi") as String)
        )
    }


    override fun onNewToken(p0: String) {
        sharedPref = MySharedPreferences(applicationContext)
        super.onNewToken(p0)
        if (sharedPref.get("access") != "") {
            GlobalScope.launch {
                WebApi.create(applicationContext!!)
                    .registerToken(
                        userFidRequest = RegisterTokenRequest(
                            sharedPref.get("uid").toString(),
                            p0
                        )
                    )
            }
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        sharedPref = MySharedPreferences(applicationContext)

        super.onMessageReceived(message)
        dataRepository = Injection.provideDataRepository(applicationContext)




        if (!((sharedPref.get("fragment") == "messages" && sharedPref.get("contactId") == message.data["value"]) ||
            ( sharedPref.get("activeWifi") != message.data["value"] && message.data["typ"] == "room") ||
            (sharedPref.get("fragment") == "roomMessages" && sharedPref.get("roomId") == message.data["value"])))
        {

            runBlocking { go() }

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

            intent.putExtra("typ", message.data["typ"])
            intent.putExtra("from", message.data["from"])
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
