package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.RoomListRequest
import com.feissenger.data.api.model.RoomMessageRequest
import com.feissenger.data.api.model.RoomReadRequest
import com.feissenger.data.db.model.RoomItem
import kotlinx.coroutines.launch

class RoomPostViewModel(private val repository: DataRepository) : ViewModel() {

    val error: MutableLiveData<String> = MutableLiveData()

    var uid: String = ""
    var roomId: String = ""

    val input: MutableLiveData<String> = MutableLiveData()

    fun sendMessage() {

        input.value?.let { it ->
            viewModelScope.launch {

                repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, it))

//                repository.notifyMessage(notifyMessage = NotificationRequest(
//                    contactFid,
//                    NotificationBody(uid, it, uid, "msg")
//                ), onError = { error.postValue(it) })
            }
        }
        input.postValue("")
    }

    fun sendGif(gif : String) {

        viewModelScope.launch {

            repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, gif))

//            repository.notifyMessage(notifyMessage = NotificationRequest(
//                contactFid,
//                NotificationBody(uid, gif, uid, "msg")
//            ), onError = { error.postValue(it) })
        }
    }
}
