package com.feissenger.ui


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.feissenger.R
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentLoginBinding
import com.feissenger.ui.viewModels.LoginViewModel
import java.lang.Exception


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(LoginViewModel::class.java)

        binding.model = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref.edit().putString("fragment","login").apply()

        val navController = findNavController()
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        viewModel.user.observeForever{
            val id: Int
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

            if(it != null){
                id = R.id.viewPagerFragment
                with (sharedPref!!.edit()) {
                    putString("access", it.access)
                    putString("refresh", it.refresh)
                    putString("uid", it.uid)
                    apply()
                }
            }else {
                id = R.id.login_fragment
                with (sharedPref!!.edit()) {
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
    }
}
