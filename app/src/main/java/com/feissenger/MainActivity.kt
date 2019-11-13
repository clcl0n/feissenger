package com.feissenger

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.feissenger.ui.ViewPagerFragmentDirections
import com.giphy.sdk.ui.GiphyCoreUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var selected = "light"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        setTheme(getSavedTheme())
        setContentView(R.layout.activity_main)
        NavigationUI.setupWithNavController(
            nav_view, Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        val navGraph = navController?.navInflater?.inflate(R.navigation.nav_graph)

        if (!getPreferences(Context.MODE_PRIVATE)?.getString("access", "").equals("")) {
            navGraph?.startDestination = R.id.viewPagerFragment
        } else {
            navGraph?.startDestination = R.id.login_fragment
        }

        navController?.graph = navGraph!!

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val image = findViewById<ImageView>(R.id.theme_icon)
        when (getPreferences(Activity.MODE_PRIVATE).getString("theme", "")) {
            "light" -> {
                image.setImageResource(R.drawable.sun)
                selected = "light"
            }
            "dark" -> {
                image.setImageResource(R.drawable.moon)
                selected = "dark"
            }
        }

        image.setOnClickListener {
            if(selected == "light"){
                selected = "dark"
                image.setImageResource(R.drawable.moon)
            }
            else{
                selected = "light"
                image.setImageResource(R.drawable.sun)
            }
            saveStringToPref("theme", selected)
            saveStringToPref("newTheme","true")
            recreate()
        }

        when (getPreferences(Activity.MODE_PRIVATE).getString("theme", "")) {
            "light" -> {
                image.setImageResource(R.drawable.sun)
                selected = "light"
            }
            "dark" -> {
                image.setImageResource(R.drawable.moon)
                selected = "dark"
            }
        }

        setSupportActionBar(findViewById(R.id.my_toolbar))

        GiphyCoreUI.configure(this, "jputsvVhTVGbajc62DSDMsoQ59MLjPdA")

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("tag", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.i("tag", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

        //creating notification channel if android version is greater than or equals to oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("simplified_coding", "Simplified Coding", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Android Push Notification Tutorial"
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        if(getPreferences(Activity.MODE_PRIVATE).getString("newTheme","") == "true"){
            getPreferences(Activity.MODE_PRIVATE).edit().putString("newTheme","false").apply()

            when(getPreferences(Activity.MODE_PRIVATE).getString("fragment","")){
                "messages"->{
                    val action = getPreferences(Activity.MODE_PRIVATE).getString("contactId","")?.let {
                        ViewPagerFragmentDirections.actionViewPagerFragmentToMessagesFragment(
                            it
                        )
                    }
                    action?.let { navController.navigate(it) }
                }
                "roomMessages"->{
                    val action = getPreferences(Activity.MODE_PRIVATE).getString("roomId","")?.let {
                        ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(
                            it
                        )
                    }
                    action?.let { navController.navigate(it) }
                }
            }

        }else{
            Log.i("theme","false")
        }


        onNewIntent(getIntent());
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    private fun saveStringToPref(key: String, value: String) {
        val editor = getPreferences(Activity.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getSavedTheme(): Int {
        return when (getPreferences(Activity.MODE_PRIVATE).getString("theme", "")) {
            "dark" -> R.style.AppThemeDark
            "light" -> R.style.AppThemeLight
            else -> R.style.AppThemeLight
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        if(extras?.get("typ").toString() == "msg"){
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToMessagesFragment(extras?.get("value").toString())
            navController?.navigate(action)
        }else{
            return
        }
    }
}
