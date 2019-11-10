package com.feissenger

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import android.app.Activity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import android.content.Context
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import androidx.navigation.ui.NavigationUI
import com.giphy.sdk.ui.GiphyCoreUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val DARK = "dark"
    private val LIGHT = "light"
    private var selected = "light"

    @SuppressLint("StringFormatInvalid")
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

        if (!getPreferences(Context.MODE_PRIVATE)?.getString("access", "").equals("")) {
            navGraph?.startDestination = R.id.room_fragment
        } else {
            navGraph?.startDestination = R.id.login_fragment
        }

        navController?.graph = navGraph!!

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

        GiphyCoreUI.configure(this, "jputsvVhTVGbajc62DSDMsoQ59MLjPdA")

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("tag", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("tag", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
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
