package com.example.viewmodel.ui.viewModels


import androidx.lifecycle.*
import com.example.viewmodel.data.DataRepository
import com.example.viewmodel.data.db.model.MessageId
import com.example.viewmodel.data.db.model.MessageItem
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
