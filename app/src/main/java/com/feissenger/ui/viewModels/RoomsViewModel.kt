package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.db.model.RoomItem
import kotlinx.coroutines.launch

class RoomsViewModel(private val repository: DataRepository) : ViewModel() {

    val error: MutableLiveData<String> = MutableLiveData()

    val activeRoom: MutableLiveData<String> = MutableLiveData()

    var uid: String = ""

    val rooms: LiveData<List<RoomItem>>
        get() = repository.getRooms(uid)

    fun setActiveRoom(active: String) {
        activeRoom.postValue(active)
    }

    fun loadRooms(access: String?, uid: String?) {
        viewModelScope.launch {
            repository.getRoomList({error.postValue(it)},access, uid)
        }
    }
}
