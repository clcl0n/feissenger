package com.feissenger.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feissenger.R
import com.feissenger.data.DataRepository
import com.feissenger.data.api.WebApi
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentContactListBinding
import com.feissenger.databinding.FragmentLoginBinding
import com.feissenger.ui.viewModels.ContactListViewModel
import com.feissenger.ui.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.Exception

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var repository: DataRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(LoginViewModel::class.java)

        binding.model = viewModel

//        adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginData.observeForever {
            if (it != null && it.access.isNotEmpty()) {
                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                if (sharedPref != null)
                    with (sharedPref.edit()) {
                        putString("access", it.access)
                        putString("refresh", it.refresh)
                        putString("uid", it.uid)
                        commit()
                    }
            }
        }

        LoginButton.setOnClickListener {
            val userName = userNameInput.text.toString()
            val password = passwordInput.text.toString()

            try {
                viewModel.login(
                    userName = userName,
                    password = password
                )
            } catch (ex: Exception) {
                Log.e("err", ex.toString())
            }
        }
    }
}
