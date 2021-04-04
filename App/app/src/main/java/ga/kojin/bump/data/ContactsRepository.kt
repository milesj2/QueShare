package ga.kojin.bump.data

import android.content.Context
import ga.kojin.bump.models.SystemContact

class ContactsRepository(context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addUser(contact: SystemContact) : Boolean = dbDriver.addContact(contact) == 1L

    fun removeUser(contact: SystemContact) : Boolean {
        TODO()
    }

    fun getContactBySystemID(id: Int) : SystemContact? = dbDriver.getContactBySystemID(id)

    fun getUsers(): ArrayList<SystemContact> = dbDriver.getContacts()

    fun getUserProfile() : SystemContact = dbDriver.getContactByID(DBDriver.USER_PROFILE_ID)!!

    fun getStarredContacts() : ArrayList<SystemContact> = dbDriver.getStarredContacts()

}