package com.feissenger.ui.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    val loginData: MutableLiveData<LoginResponse> = MutableLiveData()

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
                    throw Exception("Login failed.")
                }
            }
        } catch (ex: Exception) {
            Log.e("error", ex.toString())
        }
    }

}