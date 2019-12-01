package com.feissenger.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.feissenger.MainActivity
import com.feissenger.MySharedPreferences
import com.feissenger.R
import com.feissenger.data.util.Injection
import com.feissenger.ui.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class LogoutDialogFragment : DialogFragment() {
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        loginViewModel = ViewModelProvider(activity!!, Injection.provideViewModelFactory(activity?.applicationContext!!)).get(LoginViewModel::class.java)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.logout_dialog_text)
                .setPositiveButton(R.string.confirm_logout
                ) { _, _ ->
                    val preferences = MySharedPreferences(it.applicationContext)
                    preferences.clear()
                    loginViewModel._user.postValue(null)
                    loginViewModel._userName.postValue("")
                    loginViewModel._password.postValue("")
                    (activity as MainActivity).myToolbar.logout_icon.visibility = View.GONE

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