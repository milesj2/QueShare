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
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.util.concurrent.Executors


class ClientNetworkShare : AppCompatActivity() {

    lateinit var txtView: TextView
    private val TAG = "ClientNetworkShare"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_bump)

        txtView = findViewById(R.id.txtStatus)

        txtView.text = "Connecting"

        val uri = this.intent.data

        if (uri != null) {
            val ip = uri.getQueryParameter("host")
            val port = uri.getQueryParameter("port")?.toInt()
            val key = uri.getQueryParameter("key")
            if (ip != null && port != null && key != null) {
                GlobalScope.launch {
                    suspend {
                        sendReceive(ip, port, key)
                    }.invoke()
                }
            }
            txtView.text = "Connecting to '$ip:$port'"
        }
    }

    suspend fun sendReceive(url: String, port: Int, key: String) {
        val exec = Executors.newCachedThreadPool()
        val selector = ActorSelectorManager(exec.asCoroutineDispatcher())

        val socket = aSocket(selector).tcp().connect(InetSocketAddress(url, port))

        Log.v(TAG, "Connected...")

        val input: ByteReadChannel = socket.openReadChannel()
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)


        Log.v(TAG, "Sending Key...")
        output.writeStringUtf8("$key\r\n")

        Log.v(TAG, "Receiving Contact...")
        val response = input.readUTF8Line()

        Log.v(TAG, "Received Contact.")


        Log.v(TAG, "Sending Contact info...")
        val string: String = "${ShareHelper.buildQRString(applicationContext)}\r\n"
        output.writeStringUtf8(string)
        output.flush()

        Log.v(TAG, "Receiving Photo...")
        val imgResponse: ByteArray? = ClientSocketHelper.receiveFile(input)

        Log.v(TAG, "Sending Photo...")
        ServerSocketHelper.sendPhoto(
            output,
            PhotoRepository(this).getImageByContactID(DBDriver.USER_PROFILE_ID)
        )

        socket.close()

        val contactID: Long =
            ShareHelper.addContactFromString(applicationContext, response ?: return)
        if (imgResponse != null) {
            ShareHelper.addPhotoFromBytes(this, imgResponse, contactID)
        }
        Log.v(TAG, "Added Contact '$contactID'")

        makeToast("QueShare Successful!")
        finish()
    }

    fun makeToast(string: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, "$string", Toast.LENGTH_SHORT).show()
        }
    }
}