package ga.kojin.queshare.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWindow
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap.CompressFormat
import ga.kojin.queshare.helpers.BitmapHelper
import ga.kojin.queshare.models.SocialMediaType
import ga.kojin.queshare.models.SystemContact
import ga.kojin.queshare.models.persisted.Contact
import ga.kojin.queshare.models.persisted.Photo
import ga.kojin.queshare.models.persisted.SocialMedia
import java.io.ByteArrayOutputStream
import java.lang.reflect.Field


class DBDriver(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "data"
        const val DATABASE_VERSION = 1

        const val USER_PROFILE_ID: Long = -1

        const val TABLE_CONTACTS = "contacts"
        const val TABLE_SOCIAL_MEDIA = "social_media"
        const val TABLE_PHOTOS = "photos"

        const val KEY_CONTACT_ID = "contact_id"
        const val KEY_SYS_CONTACT_ID = "sys_contact_id"
        const val KEY_SOCIAL_MEDIA_ID = "social_media_id"
        const val KEY_FIRSTNAME = "firstname"
        const val KEY_LASTNAME = "lastname"
        const val KEY_INITIALS = "initials"
        const val KEY_MOBILE = "mobile"
        const val KEY_STARRED = "starred"

        const val KEY_SOCIAL_TYPE = "type"
        const val KEY_HANDLE = "handle"

        const val KEY_PHOTO_ID = "photo_id"
        const val KEY_PHOTO_BITMAP = "bitmap"
    }

    private val SQL_TALE_CONTACTS_DROP = "  DROP TABLE dbo.Contacts;"

    private val SQL_TABLE_CONTACTS_CREATE = "CREATE TABLE  " +
            "$TABLE_CONTACTS (" +
            "$KEY_CONTACT_ID INTEGER PRIMARY KEY, " +
            "$KEY_SYS_CONTACT_ID TEXT," +
            "$KEY_FIRSTNAME TEXT," +
            "$KEY_STARRED INTEGER," +
            "$KEY_LASTNAME TEXT," +
            "$KEY_INITIALS TEXT," +
            "$KEY_MOBILE TEXT " +
            ");"

    private val SQL_TABLE_SOCIAL_MEDIA_CREATE = "CREATE TABLE " +
            "$TABLE_SOCIAL_MEDIA (" +
            " $KEY_SOCIAL_MEDIA_ID INTEGER PRIMARY KEY" +
            ",$KEY_CONTACT_ID INTEGER" +
            ",$KEY_SOCIAL_TYPE TEXT" +
            ",$KEY_HANDLE TEXT" +
            ");"

    private val SQL_TABLE_IMAGE_CREATE = "CREATE TABLE " +
            "$TABLE_PHOTOS (" +
            " $KEY_PHOTO_ID INTEGER PRIMARY KEY " +
            ",$KEY_CONTACT_ID INTEGER" +
            ",$KEY_PHOTO_BITMAP" +
            ");"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_TABLE_CONTACTS_CREATE)
        db.execSQL(SQL_TABLE_SOCIAL_MEDIA_CREATE)
        db.execSQL(SQL_TABLE_IMAGE_CREATE)

        // Insert user profile
        val contentValues = ContentValues()
        contentValues.put(KEY_CONTACT_ID, USER_PROFILE_ID)
        contentValues.put(KEY_STARRED, false)
        contentValues.put(KEY_FIRSTNAME, "")
        contentValues.put(KEY_MOBILE, "")

        db.insert(TABLE_CONTACTS, null, contentValues)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    fun addContact(contact: Contact): Long {
        val contentValues = ContentValues()
        contentValues.put(KEY_SYS_CONTACT_ID, contact.id)
        contentValues.put(KEY_STARRED, contact.starred)
        contentValues.put(KEY_FIRSTNAME, contact.name)
        contentValues.put(KEY_MOBILE, contact.number)

        return this.writableDatabase.insert(TABLE_CONTACTS, null, contentValues)
    }

    fun getContacts(): ArrayList<Contact> {
        val list: ArrayList<Contact> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLE_CONTACTS"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                if (result.getString(result.getColumnIndex(KEY_CONTACT_ID)) == "$USER_PROFILE_ID") {
                    continue
                }
                list.add(getContactFromResult(result))
            } while (result.moveToNext())
        }
        db.close()
        return list
    }

    fun getContactByID(id: Long): Contact? {
        var contact: Contact? = null
        val db = this.readableDatabase
        val query = "Select * from $TABLE_CONTACTS WHERE $KEY_CONTACT_ID='$id'"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                contact = getContactFromResult(result)
            } while (result.moveToNext())
        }
        db.close()
        return contact
    }

    fun getContactBySystemID(id: String): Contact? {
        var contact: Contact? = null
        val db = this.readableDatabase
        val query = "Select * from $TABLE_CONTACTS WHERE $KEY_SYS_CONTACT_ID='$id'"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                contact = getContactFromResult(result)
            } while (result.moveToNext())
        }
        db.close()
        return contact
    }

    fun getStarredContacts(): ArrayList<Contact> =
        getContactResults("Select * from $TABLE_CONTACTS WHERE $KEY_STARRED='1'")

    fun updateContact(contact: Contact) {
        val contentValues = ContentValues()
        contentValues.put(KEY_STARRED, if (contact.starred) 1 else 0)
        contentValues.put(KEY_FIRSTNAME, contact.name)
        contentValues.put(KEY_MOBILE, contact.number)

        readableDatabase.update(
            TABLE_CONTACTS,
            contentValues,
            "$KEY_CONTACT_ID=?",
            arrayOf(contact.id.toString())
        )
    }

    fun deleteContact(contactID: Long) {
        val contentValues = ContentValues()
        contentValues.put(KEY_CONTACT_ID, contactID)

        readableDatabase.delete(
            TABLE_CONTACTS,
            "$KEY_CONTACT_ID=?",
            arrayOf("$contactID")
        )

    }

    private fun getContactResults(query: String): ArrayList<Contact> {
        val list: ArrayList<Contact> = ArrayList()
        val db = this.readableDatabase
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                if (result.getString(result.getColumnIndex(KEY_CONTACT_ID)) == "$USER_PROFILE_ID") {
                    continue
                }
                list.add(getContactFromResult(result))
            } while (result.moveToNext())
        }
        db.close()
        return list
    }

    private fun getSystemContactFromResult(result: Cursor): SystemContact {
        return SystemContact(
            result.getString(result.getColumnIndex(KEY_SYS_CONTACT_ID)),
            result.getString(result.getColumnIndex(KEY_FIRSTNAME)),
            result.getString(result.getColumnIndex(KEY_MOBILE)),
            result.getInt(result.getColumnIndex(KEY_STARRED)) == 1
        )
    }

    private fun getContactFromResult(result: Cursor): Contact {
        return Contact(
            result.getLong(result.getColumnIndex(KEY_CONTACT_ID)),
            result.getString(result.getColumnIndex(KEY_FIRSTNAME)),
            result.getInt(result.getColumnIndex(KEY_STARRED)) == 1,
            result.getString(result.getColumnIndex(KEY_MOBILE))
        )
    }

    fun addSocialMedia(socialMedia: SocialMedia): Long {
        val contentValues = ContentValues()
        contentValues.put(KEY_CONTACT_ID, socialMedia.contactID)
        contentValues.put(KEY_SOCIAL_TYPE, socialMedia.type.name)
        contentValues.put(KEY_HANDLE, socialMedia.handle)

        return this.writableDatabase.insert(TABLE_SOCIAL_MEDIA, null, contentValues)
    }

    fun getSocialMedia(contactID: Long?, socialMediaID: Int?): ArrayList<SocialMedia> {
        val list: ArrayList<SocialMedia> = ArrayList()
        val db = this.readableDatabase
        var query = "Select * from $TABLE_SOCIAL_MEDIA "

        if (contactID != null || socialMediaID != null) {
            query += "WHERE "
        }
        if (contactID != null) {
            query += "$KEY_CONTACT_ID='$contactID' "
        }
        if (contactID != null && socialMediaID != null) {
            query += "AND "
        }
        if (socialMediaID != null) {
            query += "$KEY_SOCIAL_MEDIA_ID='$socialMediaID' "
        }

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val social = SocialMedia(
                    result.getInt(result.getColumnIndex(KEY_SOCIAL_MEDIA_ID)),
                    result.getLong(result.getColumnIndex(KEY_CONTACT_ID)),
                    SocialMediaType.valueOf(
                        result.getString(result.getColumnIndex(KEY_SOCIAL_TYPE))
                    ),
                    result.getString(result.getColumnIndex(KEY_HANDLE))
                )
                list.add(social)
            } while (result.moveToNext())
        }
        db.close()
        return list
    }

    fun updateSocialMedia(socialMedia: SocialMedia) {
        val contentValues = ContentValues()
        contentValues.put(KEY_SOCIAL_TYPE, socialMedia.type.toString())
        contentValues.put(KEY_HANDLE, socialMedia.handle)

        readableDatabase.update(
            TABLE_SOCIAL_MEDIA,
            contentValues,
            "$KEY_SOCIAL_MEDIA_ID=?",
            arrayOf("${socialMedia.id}")
        )
    }

    fun removeSocialMedia(socialMedia: SocialMedia) {
        readableDatabase.delete(
            TABLE_SOCIAL_MEDIA,
            "$KEY_SOCIAL_MEDIA_ID=?",
            arrayOf("${socialMedia.id}")
        )
    }

    fun addImage(photo: Photo): Long {
        val contentValues = ContentValues()
        val stream = ByteArrayOutputStream()
        photo.bitmap.compress(CompressFormat.PNG, 50, stream)
        contentValues.put(KEY_PHOTO_BITMAP, stream.toByteArray())
        contentValues.put(KEY_CONTACT_ID, photo.contactID)

        return readableDatabase.insert(TABLE_PHOTOS, null, contentValues)
    }

    fun updateImage(photo: Photo): Int {
        val contentValues = ContentValues()

        contentValues.put(KEY_PHOTO_BITMAP, BitmapHelper.bitmapToBlob(photo.bitmap))
        contentValues.put(KEY_CONTACT_ID, photo.contactID)

        return readableDatabase.update(
            TABLE_PHOTOS,
            contentValues,
            "$KEY_CONTACT_ID=?",
            arrayOf("${photo.contactID}")
        )
    }

    fun upsertImage(photo: Photo): Long {
        val contentValues = ContentValues()

        contentValues.put(KEY_PHOTO_BITMAP, BitmapHelper.bitmapToBlob(photo.bitmap))
        contentValues.put(KEY_CONTACT_ID, photo.contactID)

        readableDatabase.delete(
            TABLE_PHOTOS,
            "$KEY_CONTACT_ID=?",
            arrayOf("${photo.contactID}")
        )

        return addImage(photo)
    }

    fun removeImage(photo: Photo) {

    }

    fun getImageByContactID(contactID: Long): Photo? {
        var photo: Photo? = null
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PHOTOS WHERE $KEY_CONTACT_ID='$contactID'"

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            photo = Photo(
                result.getLong(result.getColumnIndex(KEY_PHOTO_ID)),
                result.getLong(result.getColumnIndex(KEY_CONTACT_ID)),
                BitmapHelper.blobToBitMap(result.getBlob(result.getColumnIndex(KEY_PHOTO_BITMAP)))
            )
        }
        result.close()
        db.close()
        return photo


    }


}

