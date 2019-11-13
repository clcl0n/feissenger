package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.ContactMessageRequest
import com.feissenger.data.api.model.ContactReadRequest
import com.feissenger.data.api.model.NotificationBody
import com.feissenger.data.api.model.NotificationRequest
import com.feissenger.data.db.model.MessageItem
import kotlinx.coroutines.launch

class MessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    var uid: String = ""
    var contact: String = ""
    var access: String = ""

    val messages: LiveData<List<MessageItem>>
        get() = repository.getMessages(uid, contact)

    val input: MutableLiveData<String> = MutableLiveData()

    fun sendMessage() {
        input.value?.let { it ->
            val str = it
            viewModelScope.launch {

                repository.sendMessage({error.postValue(it)}, ContactMessageRequest(uid, contact, it),
                    ContactReadRequest(uid, contact), access)

                repository.notifyMessage(notifyMessage = NotificationRequest(
                    "/topics/$contact",
                    NotificationBody(uid, str)
                ), onError = { error.postValue(it) })
            }
        }
        input.postValue("")
    }

    fun loadMessages() {
        viewModelScope.launch {
            repository.loadMessages({error.postValue(it)},ContactReadRequest(uid, contact),access)
        }
    }
}
