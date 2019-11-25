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
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.feissenger.data.ConnectivityReceiver
import com.feissenger.data.util.Injection
import com.feissenger.ui.RoomsFragment
import com.feissenger.ui.ViewPagerFragmentDirections
import com.feissenger.ui.viewModels.MessagesViewModel
import com.feissenger.ui.viewModels.RoomPostViewModel
import com.feissenger.ui.viewModels.RoomsViewModel
import com.giphy.sdk.core.network.api.GPHApiClient
import com.giphy.sdk.ui.GiphyCoreUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_view_pager.*

class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {
    private lateinit var snackbar: Snackbar
    private lateinit var wifiManager: WifiManager
    private lateinit var sharedPref: MySharedPreferences
    private lateinit var roomsViewModel: RoomsViewModel
    private lateinit var messagesViewModel: MessagesViewModel
    private lateinit var roomPostViewModel: RoomPostViewModel
    private lateinit var connMgr: ConnectivityManager

    fun refresRooms(){
        roomsViewModel.uid = sharedPref.get("uid").toString()
        roomsViewModel.activeWifi = sharedPref.get("activeWifi").toString()
        roomsViewModel.setActiveRoom()
        roomsViewModel.loadRooms()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        val activeNetworkCapabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)

        if(connMgr.activeNetwork != null){
            if(activeNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                Log.i("connection", "ON WIFI")
                if (wifiManager.connectionInfo.hiddenSSID){
                    sharedPref.put("activeWifi",wifiManager.connectionInfo.bssid.removeSurrounding("\"","\""))
                    roomsViewModel.activeWifi = wifiManager.connectionInfo.bssid.removeSurrounding("\"","\"")
                    refresRooms()
                }
                else{
                    sharedPref.put("activeWifi",wifiManager.connectionInfo.ssid.removeSurrounding("\"","\""))
                    roomsViewModel.activeWifi = wifiManager.connectionInfo.ssid.removeSurrounding("\"","\"")
                    refresRooms()

                }
            }
            else{
                sharedPref.put("activeWifi","")
                refresRooms()
                Log.i("connection", "NO CONNECTION OR ONLY DATA")
            }
            snackbar.dismiss()
            messagesViewModel.enabledSend.postValue(true)
            roomPostViewModel.enableSend.postValue(true)
        }else{
            snackbar.show()
            sharedPref.put("activeWifi","")
            roomsViewModel.activeWifi = ""
            messagesViewModel.enabledSend.postValue(false)
            roomPostViewModel.enableSend.postValue(false)
            Log.i("connection", "NO CONNECTION AT ALL")
        }
        when(sharedPref.get("fragment")){
//            "roomMessages" ->
            "messages" -> messagesViewModel.loadMessages()
            "rooms", "contacts" -> {
                refresRooms()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private var selected = "light"
    private lateinit var sharedPreferences: MySharedPreferences
    lateinit var myToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext.registerReceiver(ConnectivityReceiver(), IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"))

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        roomsViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(applicationContext)).get(RoomsViewModel::class.java)
        roomPostViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(applicationContext)).get(RoomPostViewModel::class.java)
        messagesViewModel = ViewModelProvider(this, Injection.provideViewModelFactory(applicationContext)).get(MessagesViewModel::class.java)
        sharedPref = MySharedPreferences(applicationContext)

        connMgr = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

        sharedPreferences = MySharedPreferences(applicationContext)

        setTheme(getSavedTheme())
        setContentView(R.layout.activity_main)
        snackbar = Snackbar.make(app,"NO INTERNET CONNECTION!",Snackbar.LENGTH_INDEFINITE)
        snackbar.view.setBackgroundColor(Color.RED)
        NavigationUI.setupWithNavController(
            nav_view, Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
        val navGraph = navController?.navInflater?.inflate(R.navigation.nav_graph)

        if (sharedPreferences.get("access") != "") {
            navGraph?.startDestination = R.id.viewPagerFragment
        } else {
            navGraph?.startDestination = R.id.login_fragment
        }

        navController?.graph = navGraph!!

        myToolbar = findViewById(R.id.my_toolbar)

        setSupportActionBar(myToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val image = findViewById<ImageView>(R.id.theme_icon)
        when (sharedPreferences.get("theme")) {
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
            sharedPreferences.put("theme", selected)
            sharedPreferences.put("newTheme","true")
            recreate()
        }


        when (sharedPreferences.get("theme")) {
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

        logout_icon.setOnClickListener {
            sharedPreferences.clear()
            navGraph.startDestination = R.id.login_fragment
            val loginNavController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
            loginNavController?.graph = navGraph
            loginNavController?.navigate(R.id.login_fragment)
        }

        GiphyCoreUI.configure(this, "jputsvVhTVGbajc62DSDMsoQ59MLjPdA")

//

        //creating notification channel if android version is greater than or equals to oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("simplified_coding", "Simplified Coding", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Android Push Notification Tutorial"
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel)
        }

        if(sharedPreferences.get("newTheme") == "true"){
            sharedPreferences.put("newTheme","false")

            when(sharedPreferences.get("fragment")){
                "messages"->{
                    val action = ViewPagerFragmentDirections.actionViewPagerFragmentToMessagesFragment(
                        sharedPreferences.get("contactId") as String, sharedPreferences.get("contactName") as String
                        )
                    action.let { navController.navigate(it) }
                }
                "roomMessages"->{
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(
                            sharedPreferences.get("roomId") as String
                        )
                    action.let { navController.navigate(it) }
                }
                "roomsPost"->{
                    val action =
                        ViewPagerFragmentDirections.actionViewPagerFragmentToRoomPost(
                            sharedPreferences.get("roomId") as String
                        )
                    action.let { navController.navigate(it) }
                }
            }

        }else{
            Log.i("theme","false")
        }


        onNewIntent(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    private fun getSavedTheme(): Int {
        return when (sharedPreferences.get("theme")) {
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
            navController?.popBackStack(R.id.viewPagerFragment,false)
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToMessagesFragment(extras?.get("id").toString(),extras?.get("from").toString())
            navController?.navigate(action)
        }else if(extras?.get("typ").toString() == "room"){
            navController?.popBackStack(R.id.viewPagerFragment,false)
            val action = ViewPagerFragmentDirections.actionViewPagerFragmentToRoomMessagesFragment(extras?.get("id").toString())
            navController?.navigate(action)
        }
    }
}
