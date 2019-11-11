package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.api.model.LoginResponse

class SharedViewModel : ViewModel() {
    private val _user: MutableLiveData<LoginResponse> = MutableLiveData()
    val user: LiveData<LoginResponse>
        get() = _user

    fun setUser(user: LoginResponse){
        _user.postValue(user)
    }

    private val _contactId: MutableLiveData<String> = MutableLiveData()
    val contactId: LiveData<String>
        get() = _contactId

    fun setContactId(contactId: String){
        _contactId.postValue(contactId)
    }

    private val _roomId: MutableLiveData<String> = MutableLiveData()
    val roomId: LiveData<String>
        get() = _roomId

    fun setRoomId(roomId: String){
        _roomId.postValue(roomId)
    }
}
