package ga.kojin.bump.ui.contactslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsRVViewHolder : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Contacts"
    }
    val text: LiveData<String> = _text
}