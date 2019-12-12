package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.*
import kotlinx.coroutines.launch

class RoomPostViewModel(private val repository: DataRepository) : ViewModel() {

    val error: MutableLiveData<String> = MutableLiveData()

    var uid: String = ""
    var roomId: String = ""
    var senderName: String = ""
    val input_post: MutableLiveData<String> = MutableLiveData()
    var roomName: String = "Public Room"
    fun sendMessage() {

        input_post.value?.let { it ->
            viewModelScope.launch {

                repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, it))

                if(roomId == "XsTDHS3C2YneVmEW5Ry7")
                    roomName = "Public Room"
                else
                    roomName = roomId


                repository.notifyPostMessage(notifyMessage = NotificationRequest(
                    "/topics/$roomId",
                    NotificationBody(senderName,"Nový príspevok v miestnosti: $roomName",roomId,"room")
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
