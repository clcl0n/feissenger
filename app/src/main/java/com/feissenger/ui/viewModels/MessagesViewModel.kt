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

    val messages: LiveData<List<MessageItem>>
        get() = repository.getMessages(uid, contact)

    val input: MutableLiveData<String> = MutableLiveData()

    fun sendMessage() {
        input.value?.let { it ->
            viewModelScope.launch {

                repository.sendMessage({error.postValue(it)}, ContactMessageRequest(uid, contact, it),
                    ContactReadRequest(uid, contact))

                repository.notifyMessage(notifyMessage = NotificationRequest(
                    "/topics/msg_$contact",
                    NotificationBody(uid, it, uid)
                ), onError = { error.postValue(it) })
            }
        }
        input.postValue("")
    }

    fun sendGif(gif : String) {
        viewModelScope.launch {

            repository.sendMessage({error.postValue(it)}, ContactMessageRequest(uid, contact, gif),
                ContactReadRequest(uid, contact))

            repository.notifyMessage(notifyMessage = NotificationRequest(
                "/topics/msg_$contact",
                NotificationBody(uid, gif, uid)
            ), onError = { error.postValue(it) })
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            repository.loadMessages({error.postValue(it)},ContactReadRequest(uid, contact))
        }
    }
}
