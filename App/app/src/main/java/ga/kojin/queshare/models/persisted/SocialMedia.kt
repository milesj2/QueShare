package ga.kojin.queshare.models.persisted

import ga.kojin.queshare.models.SocialMediaType

data class SocialMedia(
    var id: Int,
    var contactID: Long,
    var type: SocialMediaType,
    var handle: String
)