package ga.kojin.queshare.helpers

import ga.kojin.queshare.R
import ga.kojin.queshare.models.SocialMediaType

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