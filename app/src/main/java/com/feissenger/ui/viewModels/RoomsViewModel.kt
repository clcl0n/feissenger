package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.api.model.RoomListRequest
import com.feissenger.data.db.model.RoomItem
import kotlinx.coroutines.launch

class RoomsViewModel(private val repository: DataRepository) : ViewModel() {

    val error: MutableLiveData<String> = MutableLiveData()

    val _rooms : MutableLiveData<List<RoomItem>> = MutableLiveData()
    val activeRoom: MutableLiveData<String> = MutableLiveData("")
    val visibleActive: MutableLiveData<Boolean> = MutableLiveData()

    var uid: String = ""
    var access: String = ""
    var activeWifi: String = ""


    val rooms: LiveData<List<RoomItem>>
        get() = _rooms

    fun setActiveRoom() {
        activeRoom.postValue(activeWifi)
        if(activeWifi == "")
            visibleActive.postValue(false)
        else
            visibleActive.postValue(true)
    }

    fun loadRooms() {
        viewModelScope.launch {
            repository.getRoomList({error.postValue(it)},RoomListRequest(uid))
            refreshRooms()
        }
    }

    fun refreshRooms(){
        viewModelScope.launch{
            _rooms.postValue(repository.getMutableRooms(uid, activeWifi))

        }
    }
}
