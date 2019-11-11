package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.api.model.LoginResponse

class SharedViewModel : ViewModel() {
    private val _user: MutableLiveData<LoginResponse> = MutableLiveData()
    val user: LiveData<LoginResponse>
        get() = _user

    fun setUser(user: LoginResponse?){
        _user.postValue(user)
    }

    private val _contactId: MutableLiveData<String> = MutableLiveData()
    val contactId: LiveData<String>
        get() = _contactId

    fun setContactId(contactId: String){
        _contactId.postValue(contactId)
    }

    private val _roomid: MutableLiveData<String> = MutableLiveData()
    val roomid: LiveData<String>
        get() = _roomid

    fun setRoomId(roomid: String){
        _roomid.postValue(roomid)
    }
}
