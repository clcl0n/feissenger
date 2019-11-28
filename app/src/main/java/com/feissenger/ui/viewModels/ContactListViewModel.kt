package com.feissenger.ui.viewModels


import androidx.lifecycle.*
import com.feissenger.data.DataRepository
import com.feissenger.data.db.model.ContactItem
import kotlinx.coroutines.launch

class ContactListViewModel(private val repository: DataRepository) : ViewModel() {

    val error: MutableLiveData<String> = MutableLiveData()

    val contactList: LiveData<List<ContactItem>>
        get() = repository.getContacts()

    fun loadContacts(){
        viewModelScope.launch {
            repository.getContactList{ error.postValue(it) }
        }
    }
}
