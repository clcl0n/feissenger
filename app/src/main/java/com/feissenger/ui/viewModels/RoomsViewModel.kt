package com.example.viewmodel.ui.viewModels


import androidx.lifecycle.*
import com.example.viewmodel.data.DataRepository
import com.example.viewmodel.data.db.model.MessageId
import com.example.viewmodel.data.db.model.MessageItem
import com.example.viewmodel.data.db.model.RoomItem
import kotlinx.coroutines.launch

class RoomsViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    val messages: LiveData<List<RoomItem>>
        get() = repository.getMessages()

    fun insertMessage() {
        viewModelScope.launch {
            repository.insertMessage(RoomItem(it))
        }
    }

//    fun loadMars() {
//        viewModelScope.launch {
//            repository.loadMars { error.postValue(it) }
//        }
//    }
}
