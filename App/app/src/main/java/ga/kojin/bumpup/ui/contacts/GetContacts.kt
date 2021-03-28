package ga.kojin.bumpup.ui.contacts

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import ga.kojin.bumpup.models.ContactRow


class GetContacts : Application() {
    val contacts = ArrayList<ContactRow>()

    private fun requestPermission(context: Context, permission : String, dReturn : Int){
        if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), dReturn)
        }
    }

    fun getContactList(context: Context): ArrayList<ContactRow>? {

        requestPermission(context, Manifest.permission.READ_CONTACTS, 100)

        if (context.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED)
            return null

        val cr: ContentResolver = context.contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        if ((if (cur != null) cur.getCount() else 0) > 0) {
            while (cur != null && cur.moveToNext()) {

                val id: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0)
                {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur!!.moveToNext()) {
                        // val phoneNo: String = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        val newContact = ContactRow(id, name, "MJ")
                        if (contacts.contains(newContact))
                            continue
                        contacts.add(newContact)
                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
        return contacts
    }
}