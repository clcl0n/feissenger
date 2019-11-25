package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.*
import com.feissenger.data.db.model.RoomItem
import kotlinx.coroutines.launch

class RoomPostViewModel(private val repository: DataRepository) : ViewModel() {

    val error: MutableLiveData<String> = MutableLiveData()

    var uid: String = ""
    var roomId: String = ""
    var senderName: String = ""
    val input_post: MutableLiveData<String> = MutableLiveData()

    var enableSend: MutableLiveData<Boolean> = MutableLiveData(true)

    fun sendMessage() {

        input_post.value?.let { it ->
            viewModelScope.launch {

                repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, it))

                repository.notifyPostMessage(notifyMessage = NotificationRequest(
                    "/topics/$roomId",
                    NotificationBody(senderName,"Nový príspevok v miestnosti: $roomId",roomId,"room")
                ), onError = { error.postValue(it) })

            }
        }
        input_post.postValue("")
    }

    fun sendGif(gif : String) {

        viewModelScope.launch {

            repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, gif))
            repository.notifyPostMessage(notifyMessage = NotificationRequest(
                "/topics/$roomId",
                NotificationBody(senderName,"Nový GIF súbor v miestnosti: $roomId",roomId,"room")
            ), onError = { error.postValue(it) })
        }
    }
}
