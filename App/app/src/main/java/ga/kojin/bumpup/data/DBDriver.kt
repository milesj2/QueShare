package ga.kojin.bumpup.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBDriver(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {
        var DATABASE_NAME = "data"
        private val DATABASE_VERSION = 1

        private val TABLE_CONTACTS = "contacts"

        private val KEY_ID = "id"
        private val KEY_FIRSTNAME = "name"

        /*CREATE TABLE students ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_number TEXT......);*/
        private val SQL_CREATE_USER = (
                "CREATE TABLE  " +
                "$TABLE_CONTACTS (" +
                        "$KEY_ID INTEGER PRIMARY KEY, " +
                        "$KEY_FIRSTNAME TEXT " +
                ");"
                ).trimMargin()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // db.execSQL("DROP TABLE IF EXISTS '$TABLE_CONTACTS'")
        onCreate(db)
    }



}

