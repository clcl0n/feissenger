package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.ContactMessageRequest
import com.feissenger.data.db.model.MessageItem
import kotlinx.coroutines.launch

class RoomMessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    var room: String = ""

    val messages: LiveData<List<MessageItem>>
        get() = repository.getRoomMessages(user, contact)

    val input: MutableLiveData<String> = MutableLiveData()

    fun sendMessage() {
        input.value?.let {
            var str = it
            viewModelScope.launch {
                repository.sendMessage(contactMessageRequest = ContactMessageRequest(str), onError = {error.postValue(it)})
            }
        }
        input.postValue("")
    }

    fun loadMessages() {
        viewModelScope.launch {
            repository.loadMessages {
                error.postValue(it)
            }
        }
    }
}