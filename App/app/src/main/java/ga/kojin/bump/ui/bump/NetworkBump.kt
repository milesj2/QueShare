package ga.kojin.bump.ui.bump

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ga.kojin.bump.R
import ga.kojin.bump.helpers.BumpHelper
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.util.concurrent.Executors


class NetworkBump : AppCompatActivity() {

    lateinit var txtView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_network_bump)

        txtView = findViewById(R.id.txtStatus)

        txtView.text = "Updating!"

        val uri = this.intent.data

        if (uri != null) {
            val ip = uri.getQueryParameter("host")
            val port = uri.getQueryParameter("port")?.toInt()
            if (ip != null && port != null) {
                GlobalScope.launch {
                    suspend {
                        socketstuff(ip, port)
                    }.invoke()
                }
            }
            txtView.text = "Connecting to '$ip:$port'"
        }
    }

    suspend fun socketstuff(url: String, port: Int) {
        val exec = Executors.newCachedThreadPool()
        val selector = ActorSelectorManager(exec.asCoroutineDispatcher())

        val socket = aSocket(selector).tcp().connect(InetSocketAddress(url, port))

        makeToast("Connected...")

        val input: ByteReadChannel = socket.openReadChannel()
        val output: ByteWriteChannel = socket.openWriteChannel(autoFlush = true)

        val string: String = "${BumpHelper.buildQRString(applicationContext)}\r\n"
        makeToast("Sending...")
        output.writeStringUtf8(string)

        makeToast("Receiving...")
        val response = input.readUTF8Line()
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