package ga.kojin.queshare.models

import ga.kojin.queshare.R

enum class SocialMediaType {
    Facebook,
    Snapchat,
    WhatsApp,
    Telegram;

    companion object {
        fun getTypeFromInt(type: Int): SocialMediaType = values().first { it.ordinal == type }

        fun getImageResource(type: Int): Int {
            return when (values()[type]) {
                Facebook -> R.drawable.ic_facebook
                Snapchat -> R.drawable.ic_snapchat
                WhatsApp -> R.drawable.ic_whatsapp
                Telegram -> R.drawable.ic_telegram
            }
        }

        fun getImageResource(type : SocialMediaType) : Int = getImageResource(type.ordinal)

        fun getValuesAsArray() : Array<String> {
            val list : ArrayList<String> = ArrayList()

            values().forEach {
                list.add(it.name)
            }
            return list.toArray(arrayOfNulls<String>(list.size))
        }
    }
}

