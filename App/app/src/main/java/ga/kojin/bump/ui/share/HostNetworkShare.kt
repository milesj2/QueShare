package ga.kojin.bump.ui.share

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ga.kojin.bump.R
import ga.kojin.bump.helpers.BumpHelper
import ga.kojin.bump.helpers.networking.ServerSocket
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.util.concurrent.Executors


class HostNetworkShare : AppCompatActivity() {

    lateinit var txtView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_bump)

        txtView = findViewById(R.id.txtStatus)

        txtView.text = "Connected to ${ServerSocket.connection?.remoteAddress}"

        GlobalScope.launch {
            suspend {
                socketstuff()
            }.invoke()
        }

    }

    private suspend fun socketstuff() {

        makeToast("Connected...")

        val socket = ServerSocket.connection ?: return

        val input = ServerSocket.input
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)

        val string: String = "${BumpHelper.buildQRString(applicationContext)}\r\n"

        makeToast("Sending contact...")
        output.writeStringUtf8("$string\r\n")

        makeToast("Receiving...")
        val response = (input ?: return).readUTF8Line()
        if (response != null) {
            BumpHelper.addContactFromString(applicationContext, response)
        }
        makeToast("QueShare Successful!")
        finish()
    }

    fun makeToast(string: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, "$string", Toast.LENGTH_SHORT).show()
        }
    }
}