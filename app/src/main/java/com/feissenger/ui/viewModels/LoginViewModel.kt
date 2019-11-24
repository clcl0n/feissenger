package com.feissenger.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feissenger.data.DataRepository
import com.feissenger.ui.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    val _userName: MutableLiveData<String> = MutableLiveData("")
    val _password: MutableLiveData<String> = MutableLiveData("")
    val _user : MutableLiveData<User> = MutableLiveData()

    val fid: MutableLiveData<String> = MutableLiveData()

    val error: MutableLiveData<String> = MutableLiveData()

    val userName: LiveData<String>
        get() = _userName

    val password: LiveData<String>
        get() = _password

    val user: LiveData<User>
        get() = _user

    fun login() {
        viewModelScope.launch {
            val response = repository.login(userName.value!!, password.value!!)
            if (response != null){
                response.access = "Bearer ${response.access}"
                _user.postValue(User(response.uid, response.access, response.refresh, userName.value!!))
            }
        }
    }
}