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

                } finally {
                    ServerSocketHelper.closeServer()
                }
            }.invoke()
        }

    }

    private suspend fun sendReceive(): Boolean {

        Log.v(TAG, "Connected...")

        val socket = ServerSocketHelper.connection ?: return false

        val input = ServerSocketHelper.input
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)

        updateText("Sending contact...")
        output.writeStringUtf8("${ShareHelper.serialiseProfileContact(applicationContext)}\r\n")

        updateText("Receiving Contact...")
        val contactResponse = (input ?: return false).readUTF8Line()

        updateText("Sending Photo...")
        ServerSocketHelper.sendPhoto(
            output,
            PhotoRepository(this).getImageByContactID(DBDriver.USER_PROFILE_ID)
        )

        updateText("Receiving Photo...")
        val imgResponse: ByteArray? = ClientSocketHelper.receiveFile(input, socket)

        updateText("Saving data...")
        socket.close()
        ServerSocketHelper.closeServer()

        val contactID =
            ShareHelper.deserialiseProfileContact(applicationContext, contactResponse ?: return false)

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

    fun updateText(string: String) {
        runOnUiThread {
            txtView.text = string
            Log.v(TAG, string)
        }
    }
}