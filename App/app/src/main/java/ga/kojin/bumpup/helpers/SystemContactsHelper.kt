package ga.kojin.bumpup.helpers

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import ga.kojin.bumpup.models.SystemContact


class SystemContactsHelper : Application() {
    val contacts = ArrayList<SystemContact>()

    private fun requestPermission(context: Context, permission : String, dReturn : Int){
        if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), dReturn)
        }
    }

    fun getContactList(context: Context): ArrayList<SystemContact>? {

        if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
            return null

        val cr: ContentResolver = context.contentResolver

        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        val count = cur?.count ?: 0
        if (count == 0) {
            cur?.close()
            return null
        }

        while (cur?.moveToNext() == true) {

            val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
            val name: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val starred : Boolean = 1 == cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.STARRED))

            if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
            {
                val phoneCur: Cursor? = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                if (phoneCur!!.moveToNext()) {

                    val phoneNumber: String = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val newContact = SystemContact(id, name, phoneNumber, starred)

                    if (contacts.contains(newContact))
                        continue

                    contacts.add(newContact)
                }
                phoneCur.close()
            }
        }

        cur?.close()
        return contacts
    }
}