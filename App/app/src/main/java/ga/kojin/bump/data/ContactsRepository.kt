package ga.kojin.bump.data

import android.content.Context
import ga.kojin.bump.models.SystemContact

class ContactsRepository(context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addUser(contact: SystemContact): Boolean {
        val result: Long = dbDriver.addUser(contact, null)
        return result > 0
    }

    fun removeUser(contact: SystemContact): Boolean {
        TODO()
    }

    fun getContactBySystemID(id: Int): SystemContact? = dbDriver.getContactBySystemID(id)

    fun getUsers(): ArrayList<SystemContact> = dbDriver.getContacts()


}