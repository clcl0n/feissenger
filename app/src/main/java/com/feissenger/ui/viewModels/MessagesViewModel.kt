package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.ContactMessageRequest
import com.feissenger.data.api.model.ContactReadRequest
import com.feissenger.data.api.model.NotificationBody
import com.feissenger.data.api.model.NotificationRequest
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.MessageItem
import kotlinx.coroutines.launch

class MessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    var uid: String = ""
    var contact: String = ""
    var contactName: String = ""

    val messages: LiveData<List<MessageItem>>
        get() = repository.getMessages(uid, contact)

    val input: MutableLiveData<String> = MutableLiveData()

    val contactItem: MutableLiveData<ContactItem> = MutableLiveData()

    fun sendMessage() {

        input.value?.let { it ->
            viewModelScope.launch {
                val contactFid = repository.getContactFid(contact)

                repository.sendMessage({error.postValue(it)}, ContactMessageRequest(uid, contact, it),
                    ContactReadRequest(uid, contact))

                repository.notifyMessage(notifyMessage = NotificationRequest(
                    contactFid,
                    NotificationBody(contactName,it,uid,"msg")
                ), onError = { error.postValue(it) })
            }
        }
        input.postValue("")
    }

    fun sendGif(gif : String) {

        viewModelScope.launch {
            val contactFid = repository.getContactFid(contact)

            repository.sendMessage({error.postValue(it)}, ContactMessageRequest(uid, contact, gif),
                ContactReadRequest(uid, contact))

            repository.notifyMessage(notifyMessage = NotificationRequest(
                contactFid,
                NotificationBody(contactName, "Nový GIF súbor", uid, "msg")
            ), onError = { error.postValue(it) })
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            repository.loadMessages({error.postValue(it)},ContactReadRequest(uid, contact))
        }
    }
}
