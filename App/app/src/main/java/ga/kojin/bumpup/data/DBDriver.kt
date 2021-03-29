package ga.kojin.bumpup.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ga.kojin.bumpup.models.SystemContact

class DBDriver(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {
        const val DATABASE_NAME = "data"
        const val DATABASE_VERSION = 1

        const val TABLE_CONTACTS = "contacts"

        const val KEY_ID = "id"
        const val KEY_SYS_CONTACT_ID = "sys_contact_id"
        const val KEY_FIRSTNAME = "firstname"
        const val KEY_LASTNAME = "lastname"
        const val KEY_INITIALS = "initials"
        const val KEY_MOBILE = "mobile"
        const val KEY_STARRED = "starred"
    }

    private val SQL_TALE_CONTACTS_DROP = "  DROP TABLE dbo.Contacts;"

    private val SQL_TABLE_CONTACTS_CREATE = "CREATE TABLE  " +
                                                "$TABLE_CONTACTS (" +
                                                "$KEY_ID INTEGER PRIMARY KEY, " +
                                                "$KEY_SYS_CONTACT_ID TEXT," +
                                                "$KEY_FIRSTNAME TEXT," +
                                                "$KEY_STARRED INTEGER," +
                                                "$KEY_LASTNAME TEXT," +
                                                "$KEY_INITIALS TEXT," +
                                                "$KEY_MOBILE TEXT " +
                                                ");"

    override fun onCreate(db: SQLiteDatabase) {
        // db.execSQL(SQL_TALE_CONTACTS_DROP)
        db.execSQL(SQL_TABLE_CONTACTS_CREATE)


        //addUser(ContactRow("none", "Test Name 1", "TN"), db)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    fun addUser(contact: SystemContact, db: SQLiteDatabase?): Long {

        val contentValues = ContentValues()
        contentValues.put(KEY_SYS_CONTACT_ID, contact.id)
        contentValues.put(KEY_STARRED, contact.starred)
        contentValues.put(KEY_FIRSTNAME, contact.name)
        contentValues.put(KEY_MOBILE, contact.number)

        //contentValues.put(KEY_MOBILE,mobile  )


        val success : Long
        if (db == null)
            success = this.writableDatabase.insert(TABLE_CONTACTS, null, contentValues)
        else
            success = db.insert(TABLE_CONTACTS, null, contentValues)

        return success
    }

    fun getContacts(): ArrayList<SystemContact> {
        val list: ArrayList<SystemContact> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLE_CONTACTS"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val user = SystemContact(
                    result.getString(result.getColumnIndex(KEY_SYS_CONTACT_ID)),
                    result.getString(result.getColumnIndex(KEY_FIRSTNAME)),
                    result.getString(result.getColumnIndex(KEY_MOBILE)),
                    result.getInt(result.getColumnIndex(KEY_STARRED)) == 1
                )
                list.add(user)
            }
            while (result.moveToNext())
        }
        db.close()
        return list
    }


}

