package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.RoomMessageRequest
import com.feissenger.data.api.model.RoomReadRequest
import com.feissenger.data.db.model.RoomMessageItem
import kotlinx.coroutines.launch

class RoomMessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    var roomid: String = ""
    var uid : String = ""
    var access : String = ""
    val roomMessage: MutableLiveData<String> = MutableLiveData()

    val messages: LiveData<List<RoomMessageItem>>
        get() = repository.getRoomMessages(roomid)

    val input: MutableLiveData<String> = MutableLiveData()

    fun sendRoomMessage() {
        input.value?.let { it ->
            viewModelScope.launch {
                repository.sendRoomMessage({error.postValue(it)}, RoomMessageRequest(uid, roomid,
                    roomMessage.value!!
                ), access
                )
            }
        }
        input.postValue("")
    }

    fun loadRoomMessages() {
        viewModelScope.launch {
            repository.loadRoomMessages({
                error.postValue(it)
            },RoomReadRequest(uid,roomid),access)
        }
    }
}
