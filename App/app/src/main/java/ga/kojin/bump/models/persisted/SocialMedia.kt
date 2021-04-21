package ga.kojin.bump.models.persisted

import ga.kojin.bump.models.SocialMediaType

data class SocialMedia(
    var id: Int,
    var contactID: Long,
    var type: SocialMediaType,
    var handle: String
    ) {
}