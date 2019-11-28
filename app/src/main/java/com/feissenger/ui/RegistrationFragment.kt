package com.feissenger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.feissenger.R
import com.feissenger.data.util.Injection
import com.feissenger.databinding.FragmentRegistrationBinding
import com.feissenger.ui.viewModels.RegistrationViewModel
import androidx.navigation.fragment.findNavController

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var viewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_registration, container, false
        )
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(context!!))
            .get(RegistrationViewModel::class.java)

        viewModel.notMatchingPasswordMessage = getString(R.string.not_matching_passwords)
        viewModel.existingUserNameMessage = getString(R.string.existing_username_message)
        binding.model = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        viewModel.user.observeForever {
            if (it != null) {
                navController.navigate(R.id.login_fragment)
            }
        }
    }
}