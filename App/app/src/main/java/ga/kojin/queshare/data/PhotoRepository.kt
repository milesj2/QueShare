package ga.kojin.queshare.data

import android.content.Context
import ga.kojin.queshare.models.persisted.Photo

class PhotoRepository (context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addImage(photo: Photo) = dbDriver.addImage(photo)

    fun updateImage(photo: Photo) = dbDriver.updateImage(photo) == 1

    fun upsertContactImage(photo: Photo) {
        dbDriver.upsertImage(photo)
    }

    fun removeImageByContactID(photo: Photo) {
        dbDriver.removeImage(photo)
    }

    fun getImageByContactID(contactID: Long): Photo? = dbDriver.getImageByContactID(contactID)

}