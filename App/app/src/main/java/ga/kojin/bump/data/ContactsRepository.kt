package ga.kojin.bump.data

import android.content.Context
import ga.kojin.bump.data.DBDriver
import ga.kojin.bump.models.SystemContact
import ga.kojin.bump.models.persisted.Contact

class ContactsRepository(context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addUser(contact: Contact) : Boolean = dbDriver.addContact(contact) == 1L

    fun removeUser(contact: SystemContact) : Boolean {
        TODO()
    }

    fun getContactBySystemID(id: String): Contact? = dbDriver.getContactBySystemID(id)

    fun getContactByID(id: Long): Contact? = dbDriver.getContactByID(id)

    fun getUsers(): ArrayList<Contact> = dbDriver.getContacts()

    fun getUserProfile(): Contact = dbDriver.getContactByID(DBDriver.USER_PROFILE_ID)!!

    fun getStarredContacts(): ArrayList<Contact> = dbDriver.getStarredContacts()

    fun setContactFavouriteStatus(contactID: Long, status: Boolean) {
        val contact = dbDriver.getContactByID(contactID) ?: return
        contact.starred = status
        dbDriver.updateContact(contact)
    }

    fun updateContact(contact: Contact) = dbDriver.updateContact(contact)

    fun deleteContact(contactID: Long) = dbDriver.deleteContact(contactID)



}