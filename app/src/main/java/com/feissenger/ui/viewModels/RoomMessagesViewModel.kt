package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.RoomReadRequest
import com.feissenger.data.db.model.RoomMessageItem
import kotlinx.coroutines.launch

class RoomMessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    var roomid: String = ""
    var uid : String = ""

    var showFab: MutableLiveData<Boolean> = MutableLiveData(true)

    val messages: LiveData<List<RoomMessageItem>>
        get() = repository.getRoomMessages(roomid)

    fun loadRoomMessages() {
        viewModelScope.launch {
            repository.loadRoomMessages({
                error.postValue(it)
            },RoomReadRequest(uid,roomid))
        }
    }
}
