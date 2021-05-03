package ga.kojin.bump.models.persisted

import android.graphics.Bitmap

data class Photo(
    val id: Long,
    var contactID: Long,
    var bitmap: Bitmap,
)
