package ga.kojin.bumpup.data

import android.app.Application
import android.content.Context
import ga.kojin.bumpup.models.ContactRow

class ContactsRepository(context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addUser(contact: ContactRow):Boolean {
        val result: Long = dbDriver.addUser(contact, null)
        return result > 0
    }

    fun removeUser(contact: ContactRow): Boolean {
        TODO()
    }

    fun getUser(id: Int): ContactRow {
        TODO()
    }

    fun getUsers(): ArrayList<ContactRow> {
        return dbDriver.getContacts()
    }

}