package ga.kojin.bump.helpers

import ga.kojin.bump.R
import ga.kojin.bump.models.SocialMediaType
import ga.kojin.bump.models.persisted.SocialMedia

object SocialMediaTypeHelper {


    fun getTypeFromInt(type: Int) : SocialMediaType {
        return SocialMediaType.values()[type]
    }

    fun getImageResource(type : Int) : Int {
        return when(SocialMediaType.values()[type]){
            SocialMediaType.Facebook -> R.drawable.ic_facebook
            SocialMediaType.Snapchat -> R.drawable.ic_snapchat
            SocialMediaType.WhatsApp -> R.drawable.ic_whatsapp
            SocialMediaType.Telegram -> R.drawable.ic_telegram
        }
    }

}