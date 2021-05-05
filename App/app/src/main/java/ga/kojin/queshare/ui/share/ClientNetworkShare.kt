package ga.kojin.queshare.ui.share

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
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
import java.lang.Exception
import java.net.InetSocketAddress
import java.util.concurrent.Executors


class ClientNetworkShare : AppCompatActivity() {

    private lateinit var txtView: TextView
    private lateinit var imgHost: ImageView
    private lateinit var imgClient: ImageView
    private val TAG = "ClientNetworkShare"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_bump)

        txtView = findViewById(R.id.txtStatus)
        imgClient = findViewById(R.id.imgClient)
        imgHost = findViewById(R.id.imgHost)

        imgHost.visibility = View.GONE

        txtView.text = "Connecting"

        val uri = this.intent.data

        if (uri != null) {
            val ip = uri.getQueryParameter("host")
            val port = uri.getQueryParameter("port")?.toInt()
            val key = uri.getQueryParameter("key")
            if (ip != null && port != null && key != null) {
                Log.v(TAG, "connecting to $ip:$port with key:$key")
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
        updateText("Connecting to device...")

        val socket: Socket

        try {
            val exec = Executors.newCachedThreadPool()
            val selector = ActorSelectorManager(exec.asCoroutineDispatcher())
            socket =
                aSocket(selector).tcp().connect(InetSocketAddress(url, port))
        } catch (e: Exception) {
            makeToast("Failed to connect to device. Are you on the same Wi-Fi?")
            Log.e(TAG, "ERROR")
            Log.e(TAG, "${e.cause}\n${e.message}\n${e.stackTrace}")
            finish()
            return
        }
        updateText("Connected...")
        showHost()

        val input: ByteReadChannel = socket.openReadChannel()
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)


        updateText("Sending Key...")
        output.writeStringUtf8("$key\r\n")

        updateText("Receiving Contact...")
        val response = input.readUTF8Line()

        updateText("Received Contact.")


        updateText("Sending Contact info...")
        val string: String = "${ShareHelper.serialiseProfileContact(applicationContext)}\r\n"
        output.writeStringUtf8(string)
        output.flush()

        updateText("Receiving Photo...")
        val imgResponse: ByteArray? = ClientSocketHelper.receiveFile(input, socket)

        updateText("Sending Photo...")
        ServerSocketHelper.sendPhoto(
            output,
            PhotoRepository(this).getImageByContactID(DBDriver.USER_PROFILE_ID)
        )
        socket.close()

        updateText("Saving data...")
        val contactID: Long =
            ShareHelper.deserialiseProfileContact(applicationContext, response ?: return)
        if (imgResponse != null) {
            ShareHelper.addPhotoFromBytes(this, imgResponse, contactID)
        }

        makeToast("QueShare Successful!")
        finish()
    }

    private fun makeToast(string: String) {
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

    fun showHost() {
        runOnUiThread {
            imgHost.visibility = View.VISIBLE
        }
    }
}