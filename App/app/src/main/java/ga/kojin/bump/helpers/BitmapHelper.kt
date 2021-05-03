package ga.kojin.bump.helpers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object BitmapHelper {
    fun bitmapToBlob(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun blobToBitMap(byteArray: ByteArray) =
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

}