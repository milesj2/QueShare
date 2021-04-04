package ga.kojin.bump.data

import android.content.Context
import ga.kojin.bump.models.SystemContact
import ga.kojin.bump.models.persisted.SocialMedia

class SocialMediaRepository (context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addSocialMedia(socialMedia: SocialMedia) : Boolean =
        dbDriver.addSocialMedia(socialMedia) == 1L


    fun removeSocialMedia(socialMedia: SocialMedia){
        TODO("NotImplemented")
    }

    fun getSocialMediaByID(id: Int) : SocialMedia? {
        val rows = dbDriver.getSocialMedia(null, id)
        return if (rows.size == 0) {
            null
        } else {
            rows[0]
        }
    }

    fun getSocialMediaByContactID(contactID: String) : ArrayList<SocialMedia> =
        dbDriver.getSocialMedia(contactID, null)

    fun getAllSocialMedia() : List<SocialMedia> =
        dbDriver.getSocialMedia(null, null)
}