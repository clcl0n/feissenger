package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.RoomMessageRequest
import com.feissenger.data.api.model.RoomReadRequest
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.RoomItem
import com.feissenger.data.db.model.RoomMessageItem
import kotlinx.coroutines.launch

class RoomMessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    var roomid: String = ""
    var uid : String = ""

    val roomMessage: MutableLiveData<String> = MutableLiveData()

    val messages: LiveData<List<RoomMessageItem>>
        get() = repository.getRoomMessages(uid, roomid)

    val input: MutableLiveData<String> = MutableLiveData()

    val roomName: MutableLiveData<String> = MutableLiveData()

    fun sendRoomMessage() {
        input.value?.let {
            viewModelScope.launch {
                repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomid,
                    roomMessage.value!!
                )
                )
            }
        }
        input.postValue("")
    }

    fun loadRoomMessages() {
        viewModelScope.launch {
            repository.loadRoomMessages({
                error.postValue(it)
            },RoomReadRequest(uid,roomid))
        }
    }
}
