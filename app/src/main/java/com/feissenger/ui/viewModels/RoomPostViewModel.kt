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

    val input_post: MutableLiveData<String> = MutableLiveData()

    var enableSend: MutableLiveData<Boolean> = MutableLiveData(true)

    fun sendMessage() {

        input_post.value?.let { it ->
            viewModelScope.launch {

                repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, it))
            }
        }
        input_post.postValue("")
    }

    fun sendGif(gif : String) {

        viewModelScope.launch {

            repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomId, gif))
        }
    }
}
