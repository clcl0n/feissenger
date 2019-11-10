package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import androidx.navigation.Navigation.findNavController
import com.feissenger.R
import com.feissenger.data.DataRepository

class SharedViewModel() : ViewModel() {
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
