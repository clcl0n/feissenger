package com.feissenger

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
import androidx.lifecycle.ViewModelProviders
import android.os.Build
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.feissenger.data.api.model.LoginResponse
import com.feissenger.ui.viewModels.SharedViewModel
import com.giphy.sdk.ui.GiphyCoreUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private val DARK = "dark"
    private val LIGHT = "light"
    private var selected = "light"

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        sharedViewModel = run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        }

        if (!getPreferences(Context.MODE_PRIVATE)?.getString("access", "").equals("")) {
            navGraph?.startDestination = R.id.viewPagerFragment

            with(getPreferences(Context.MODE_PRIVATE)){
                getString("uid","")?.let {
                    getString("access","")?.let { it1 ->
                        getString("refresh","")?.let { it2 ->
                            LoginResponse(it, it1, it2)
                        }
                    }
                }?.let { sharedViewModel.setUser(it) }
            }
        } else {
            navGraph?.startDestination = R.id.login_fragment
        }

        navController?.graph = navGraph!!

        sharedViewModel.user.observeForever{
            val id: Int
            val sharedPref = getPreferences(Context.MODE_PRIVATE)

            if(it != null){
                id = R.id.viewPagerFragment
                with (sharedPref.edit()) {
                    putString("access", it.access)
                    putString("refresh", it.refresh)
                    putString("uid", it.uid)
                    apply()
                }
            }else {
                id = R.id.login_fragment
                with (sharedPref.edit()) {
                    putString("access", "")
                    putString("refresh", "")
                    putString("uid", "")
                    apply()
                }
            }

            navGraph.startDestination = id
            navController.graph = navGraph
            navController.navigate(id)
        }

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val image = findViewById<ImageView>(R.id.theme_icon)
        when (getPreferences(Activity.MODE_PRIVATE).getString("theme", LIGHT)) {
            LIGHT -> {
                image.setImageResource(R.drawable.sun)
                selected = "light"
            }
            DARK -> {
                image.setImageResource(R.drawable.moon)
                selected = "dark"
            }
        }

        image.setOnClickListener {
            if(selected == "light"){
                selected = "dark"
                image.setImageResource(R.drawable.moon)
                saveTheme(DARK)
            }
            else{
                selected = "light"
                image.setImageResource(R.drawable.sun)
                saveTheme(LIGHT)
            }
        }

        when (getPreferences(Activity.MODE_PRIVATE).getString("theme", LIGHT)) {
            LIGHT -> {
                image.setImageResource(R.drawable.sun)
                selected = "light"
            }
            DARK -> {
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
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    private fun saveTheme(value: String) {
        val editor = getPreferences(Activity.MODE_PRIVATE).edit()
        editor.putString("theme", value)
        editor.apply()
        recreate()
    }

    private fun getSavedTheme(): Int {
        return when (getPreferences(Activity.MODE_PRIVATE).getString("theme", LIGHT)) {
            DARK -> R.style.AppThemeDark
            LIGHT -> R.style.AppThemeLight
            else -> R.style.AppThemeLight
        }
    }
}
