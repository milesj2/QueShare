package ga.kojin.bump.ui.bump.qrshare

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import ga.kojin.bump.R

class QRShareActivity : AppCompatActivity() {

    private val TAG : String = "QRShareActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Activity started")

        setContentView(R.layout.activity_qr_share)

        val btnNavBack : ImageView = findViewById(R.id.imgNavBack)
        btnNavBack.setOnClickListener {
            finish()
        }

        val imgQRCode : ImageView = findViewById(R.id.imgQRCode)
        imgQRCode.setImageBitmap(generateQRCode(buildQRString()))
    }

    private fun buildQRString(): String {
        return "Example QER CODE"
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 1000
        val height = 1000
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
    }

}