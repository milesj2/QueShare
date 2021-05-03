package ga.kojin.bump.ui.share.qrshare

import android.graphics.Bitmap
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter.formatIpAddress
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.data.SocialMediaRepository
import ga.kojin.bump.helpers.BumpHelper
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.util.*


class QRShareActivity : AppCompatActivity() {

    private val TAG : String = "QRShareActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v(TAG, "Activity started")

        setContentView(R.layout.activity_qr_share)

        val btnNavBack: ImageView = findViewById(R.id.imgNavBack)
        btnNavBack.setOnClickListener {
            finish()
        }

        val imgQRCode: ImageView = findViewById(R.id.imgQRCode)
        // imgQRCode.setImageBitmap(generateQRCode(buildQRString()))

        val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val ip = formatIpAddress(wm.connectionInfo.ipAddress)
        val port = Random().nextInt(9999 - 1000) + 1000

        imgQRCode.setImageBitmap(generateQRCode("bump://ga.kojin.bump/share?host=$ip&port=$port&key=1234"))

        GlobalScope.launch {
            suspend {
                setupSocket(ip, port)
            }.invoke()
        }


    }

    private fun buildQRString(): String {

        val contact = ContactsRepository(applicationContext).getUserProfile()

        contact.socialMedia = SocialMediaRepository(applicationContext)
            .getSocialMediaByContactID(contact.id)

        val mapper = jacksonObjectMapper()

        return mapper.writeValueAsString(contact)
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

    private suspend fun setupSocket(url: String, port: Int) {
        Log.v(TAG, "Starting Server")
        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .bind(InetSocketAddress(url, port))

        while (true) {
            Log.v(TAG, "Listening")
            val socket = server.accept()
            Log.v(TAG, "Found new connection")
            val input = socket.openReadChannel()
            val output = socket.openWriteChannel(autoFlush = true)

            try {
                while (true) {
                    Log.i(TAG, "reading")
                    val line = input.readUTF8Line()
                    Log.i(TAG, "Received input '$line'")
                    output.writeStringUtf8("${BumpHelper.buildQRString(applicationContext)}}\r\n")
                    runOnUiThread {
                        Toast.makeText(applicationContext, line, Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                socket.close()
            }
        }
    }

}