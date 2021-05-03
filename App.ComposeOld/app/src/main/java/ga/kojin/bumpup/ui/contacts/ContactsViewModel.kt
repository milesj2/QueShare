package ga.kojin.bumpup.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Contacts"
    }
    val text: LiveData<String> = _text
}