package ga.kojin.bump.models.persisted

import ga.kojin.bump.models.SocialMediaType

data class SocialMedia(
    var id: Int,
    var contactID: String,
    var type: SocialMediaType,
    var handle: String
    ) {
}