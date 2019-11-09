package com.feissenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import android.view.Menu
import android.widget.Switch
import android.app.Activity
import androidx.appcompat.widget.SwitchCompat
import androidx.navigation.ui.NavigationUI
import com.giphy.sdk.ui.GPHContentType
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.GiphyCoreUI
import com.giphy.sdk.ui.themes.GridType
import com.giphy.sdk.ui.themes.LightTheme
import com.giphy.sdk.ui.views.GiphyDialogFragment
import com.giphy.sdk.ui.views.buttons.GPHGiphyButton
import com.giphy.sdk.ui.views.buttons.GPHGiphyButtonStyle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_message.*


class MainActivity : AppCompatActivity() {

    private val DARK = "dark"
    private val LIGHT = "light"

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
        setSupportActionBar(findViewById(R.id.my_toolbar))

        GiphyCoreUI.configure(this, "jputsvVhTVGbajc62DSDMsoQ59MLjPdA")
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu, this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val switchItem = menu.findItem(R.id.switch_theme)
        val switch = switchItem?.actionView as SwitchCompat
        when (getPreferences(Activity.MODE_PRIVATE).getString("theme", DARK)) {
            LIGHT -> switch.isChecked = true
            DARK -> switch.isChecked = false
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                saveTheme(LIGHT)
            else
                saveTheme(DARK)
        }
        return true
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
