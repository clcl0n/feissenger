package com.feissenger.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.feissenger.MySharedPreferences
import com.feissenger.R

class LogoutDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.logout_dialog_text)
                .setPositiveButton(R.string.confirm_logout
                ) { _, _ ->
                    val preferences = MySharedPreferences(it.applicationContext)
                    preferences.clear()

                    val navController = it.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
                    val navGraph = navController?.navInflater?.inflate(R.navigation.nav_graph)

                    navGraph?.startDestination = R.id.login_fragment
                    navController?.graph = navGraph!!
                    navController.navigate(R.id.login_fragment)
                }
                .setNegativeButton(R.string.cancel_logout
                ) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}