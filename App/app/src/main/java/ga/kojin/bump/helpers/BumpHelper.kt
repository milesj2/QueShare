package ga.kojin.bump.helpers

import android.content.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.data.SocialMediaRepository
import ga.kojin.bump.models.persisted.Contact

object BumpHelper {

    fun buildQRString(context: Context): String {

        val contact = ContactsRepository(context).getUserProfile()

        contact.socialMedia = SocialMediaRepository(context)
            .getSocialMediaByContactID(contact.id)

        val mapper = jacksonObjectMapper()

        return mapper.writeValueAsString(contact)
    }

    fun addContactFromString(context: Context, text: String) {
        val mapper = jacksonObjectMapper()
        val contact = mapper.readValue<Contact>(text)
        val userID = ContactsRepository(context).addContact(contact)
        val socialMediaRepository = SocialMediaRepository(context)
        contact.socialMedia?.forEach {
            it.contactID = userID
            socialMediaRepository.addSocialMedia(it)
        }
    }

}