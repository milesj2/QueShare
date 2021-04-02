package ga.kojin.bumpup.data

import android.content.Context
import ga.kojin.bumpup.models.SystemContact

class ContactsRepository(context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addUser(contact: SystemContact):Boolean {
        val result: Long = dbDriver.addUser(contact, null)
        return result > 0
    }

    fun removeUser(contact: SystemContact): Boolean {
        TODO()
    }

    fun getUser(id: Int): SystemContact {
        TODO()
    }

    fun getUsers(): ArrayList<SystemContact> {
        return dbDriver.getContacts()
    }

}