package ga.kojin.bump.models.persisted


data class Contact(
    val id: Long,
    var name: String,
    var starred: Boolean,
    var number: String,
    var socialMedia: ArrayList<SocialMedia>? = null
)