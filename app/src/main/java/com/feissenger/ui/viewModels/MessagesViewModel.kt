package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.db.model.MessageItem
import kotlinx.coroutines.launch

class MessagesViewModel(private val repository: DataRepository) : ViewModel() {
    val error: MutableLiveData<String> = MutableLiveData()

    val messages: LiveData<List<MessageItem>>
        get() = repository.getMessages()

    val input: MutableLiveData<String> = MutableLiveData()

    fun insertMessage() {
        input.value?.let {
            viewModelScope.launch {
                repository.insertMessage(MessageItem(it))
            }
        }
        input.postValue("")
    }

//    fun loadMars() {
//        viewModelScope.launch {
//            repository.loadMars { error.postValue(it) }
//        }
//    }
}
