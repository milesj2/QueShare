package ga.kojin.queshare.ui.share

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ga.kojin.queshare.R
import ga.kojin.queshare.data.DBDriver
import ga.kojin.queshare.data.PhotoRepository
import ga.kojin.queshare.helpers.ShareHelper
import ga.kojin.queshare.helpers.networking.ClientSocketHelper
import ga.kojin.queshare.helpers.networking.ServerSocketHelper
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


class HostNetworkShare : AppCompatActivity() {

    lateinit var txtView: TextView
    private val TAG = "HostNetworkShare"
    val bufferSize = 4096

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_bump)

        txtView = findViewById(R.id.txtStatus)

        txtView.text = "Connected to ${ServerSocketHelper.connection?.remoteAddress}"

        GlobalScope.launch {
            suspend {
                try {
                    if (sendReceive()) {
                        finish()
                    }
                } catch (e: Exception) {

                }
            }.invoke()
        }

    }

    private suspend fun sendReceive(): Boolean {

        Log.v(TAG, "Connected...")

        val socket = ServerSocketHelper.connection ?: return false

        val input = ServerSocketHelper.input
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)

        val string: String = "${ShareHelper.buildQRString(applicationContext)}\r\n"

        Log.v(TAG, "Sending Contact info...")
        output.writeStringUtf8("$string\r\n")

        Log.v(TAG, "Receiving Contact...")
        val contactResponse = (input ?: return false).readUTF8Line()


        Log.v(TAG, "Sending Photo...")
        ServerSocketHelper.sendPhoto(
            output,
            PhotoRepository(this).getImageByContactID(DBDriver.USER_PROFILE_ID)
        )

        Log.v(TAG, "Receiving Photo...")
        val imgResponse: ByteArray? = ClientSocketHelper.receiveFile(input)

        socket.close()

        val contactID =
            ShareHelper.addContactFromString(applicationContext, contactResponse ?: return false)

        if (imgResponse != null) {
            ShareHelper.addPhotoFromBytes(this, imgResponse, contactID)
        }
        makeToast("QueShare Successful!")
        return true
    }

    fun makeToast(string: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, "$string", Toast.LENGTH_SHORT).show()
        }
    }
}