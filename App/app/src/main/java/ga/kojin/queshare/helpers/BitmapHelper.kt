package ga.kojin.queshare.helpers

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import java.io.ByteArrayOutputStream


object BitmapHelper {

    fun bitmapToBlob(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun blobToBitMap(byteArray: ByteArray) =
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)


    fun cropBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap =
        Bitmap.createBitmap(bitmap, 0, 0, width, height)


    @SuppressLint("NewApi")
    fun blurBitmap(context: Context?, smallBitmap: Bitmap, radius: Int): Bitmap? {
        var smallBitmap = smallBitmap
        try {
            smallBitmap = RGB565toARGB888(smallBitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bitmap = Bitmap.createBitmap(
            smallBitmap.width, smallBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val renderScript = RenderScript.create(context)
        val blurInput = Allocation.createFromBitmap(renderScript, smallBitmap)
        val blurOutput = Allocation.createFromBitmap(renderScript, bitmap)
        val blur = ScriptIntrinsicBlur.create(
            renderScript,
            Element.U8_4(renderScript)
        )
        blur.setInput(blurInput)
        blur.setRadius(radius.toFloat()) // radius must be 0 < r <= 25
        blur.forEach(blurOutput)
        blurOutput.copyTo(bitmap)
        renderScript.destroy()
        return bitmap
    }

    private fun RGB565toARGB888(img: Bitmap): Bitmap {
        val numPixels = img.width * img.height
        val pixels = IntArray(numPixels)
        img.getPixels(pixels, 0, img.width, 0, 0, img.width, img.height)
        val result = Bitmap.createBitmap(img.width, img.height, Bitmap.Config.ARGB_8888)
        result.setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        return result
    }

    fun resizeBitmap(
        img: Bitmap,
        width: Int,
        height: Int,
        filter: Boolean,
        lockRatio: Boolean
    ): Bitmap {
        if (!lockRatio) {
            return Bitmap.createScaledBitmap(img, width, height, filter)
        }
        val ratio = img.width / img.height
        val newBitmap = Bitmap.createScaledBitmap(img, width, width / ratio, filter)
        return cropBitmap(newBitmap, width, height)
    }

    fun compressBitmapToStream(img: Bitmap, quality: Int): ByteArrayOutputStream {
        val stream = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, quality, stream)
        return stream
    }
}