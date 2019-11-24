package com.feissenger.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    val loginData: MutableLiveData<LoginResponse> = MutableLiveData()
    val _userInfo: MutableLiveData<String> = MutableLiveData("")
    var wrongUsernameOrPasswordMessage = ""

    fun login(userName: String, password: String) {
        try {
            viewModelScope.launch {
                val loginResponse = repository.login(
                    userName = userName,
                    password = password
                )

                if (loginResponse != null) {
                    loginData.postValue(loginResponse)
                } else {
                    _userInfo.postValue(wrongUsernameOrPasswordMessage)
                }
            }
        } catch (ex: Exception) {
            Log.e("error", ex.toString())
        }
    }

}