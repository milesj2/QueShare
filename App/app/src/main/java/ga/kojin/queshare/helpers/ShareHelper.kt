package ga.kojin.queshare.helpers

import android.content.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ga.kojin.queshare.data.ContactsRepository
import ga.kojin.queshare.data.PhotoRepository
import ga.kojin.queshare.data.SocialMediaRepository
import ga.kojin.queshare.models.persisted.Contact
import ga.kojin.queshare.models.persisted.Photo

object ShareHelper {

    fun serialiseProfileContact(context: Context): String {

        val contact = ContactsRepository(context).getUserProfile()

        contact.socialMedia = SocialMediaRepository(context)
            .getSocialMediaByContactID(contact.id)

        val mapper = jacksonObjectMapper()

        return mapper.writeValueAsString(contact)
    }

    fun deserialiseProfileContact(context: Context, text: String): Long {
        val mapper = jacksonObjectMapper()
        val contact = mapper.readValue<Contact>(text)
        val userID = ContactsRepository(context).addContact(contact)
        val socialMediaRepository = SocialMediaRepository(context)
        contact.socialMedia?.forEach {
            it.contactID = userID
            socialMediaRepository.addSocialMedia(it)
        }
        return userID
    }

    fun addPhotoFromBytes(context: Context, byteArray: ByteArray, contactID: Long) {
        PhotoRepository(context).addImage(
            Photo(
                -1,
                contactID,
                BitmapHelper.blobToBitMap(byteArray)
            )
        )
    }

}