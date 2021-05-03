package ga.kojin.bump.data

import android.content.Context
import ga.kojin.bump.data.DBDriver
import ga.kojin.bump.models.persisted.SocialMedia

class SocialMediaRepository (context: Context) {

    private val dbDriver: DBDriver = DBDriver(context)

    fun addSocialMedia(socialMedia: SocialMedia): Long = dbDriver.addSocialMedia(socialMedia)

    fun removeSocialMedia(socialMedia: SocialMedia) {
        dbDriver.removeSocialMedia(socialMedia)
    }

    fun getSocialMediaByID(id: Int): SocialMedia? {
        val rows = dbDriver.getSocialMedia(null, id)
        return if (rows.size == 0) {
            null
        } else {
            rows[0]
        }
    }

    fun getSocialMediaByContactID(contactID: Long) : ArrayList<SocialMedia> =
        dbDriver.getSocialMedia(contactID, null)

    fun getAllSocialMedia(): List<SocialMedia> =
        dbDriver.getSocialMedia(null, null)

    fun updateSocialMedia(social: SocialMedia) = dbDriver.updateSocialMedia(social)
}