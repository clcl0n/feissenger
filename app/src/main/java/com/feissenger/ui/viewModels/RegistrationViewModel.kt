package com.feissenger.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.RegisterResponse
import kotlinx.coroutines.launch

class RegistrationViewModel(private val repository: DataRepository) : ViewModel() {
    val _password: MutableLiveData<String> = MutableLiveData("")
    val _passwodConfirm: MutableLiveData<String> = MutableLiveData("")
    val _userName: MutableLiveData<String> = MutableLiveData("")
    val _userInfo: MutableLiveData<String> = MutableLiveData("")
    val _user : MutableLiveData<RegisterResponse> = MutableLiveData()
    var notMatchingPasswordMessage: String = ""
    var existingUserNameMessage: String = ""

    val userName: LiveData<String>
        get() = _userName

    val password: LiveData<String>
        get() = _password

    val passwodConfirm: LiveData<String>
        get() = _passwodConfirm

    val user: LiveData<RegisterResponse>
        get() = _user

    fun registration() {
        if (password.value!! == passwodConfirm.value!!) {
            viewModelScope.launch {
                val response = repository.register(
                    userName = userName.value!!,
                    password = password.value!!
                )

                if (response == null) {
                    _userInfo.postValue(existingUserNameMessage)
                } else {
                    _user.postValue(response)
                }
            }
        } else {
            _userInfo.postValue(notMatchingPasswordMessage)
        }
    }
}