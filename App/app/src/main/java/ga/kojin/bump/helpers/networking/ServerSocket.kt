package ga.kojin.bump.helpers.networking

import android.util.Log
import android.widget.Toast
import ga.kojin.bump.helpers.BumpHelper
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress

object ServerSocket {

    var connection: Socket? = null
    var input: ByteReadChannel? = null

    private val TAG: String = "ServerSocket"


    suspend fun setupSocket(
        url: String,
        port: Int,
        key: String,
        onConnect: () -> Unit
    ) {
        Log.v(TAG, "Starting Server...")
        val server = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .bind(InetSocketAddress(url, port))

        while (connection == null) {
            Log.v(TAG, "Listening...")
            val socket = server.accept()

            Log.v(TAG, "Found new connection '${socket.remoteAddress}'.")
            input = socket.openReadChannel()

            try {
                while (true) {
                    Log.i(TAG, "Waiting for input...")
                    val line = input!!.readUTF8Line()
                    Log.i(TAG, "Received input '$line'")

                    if (line == key) {
                        connection = socket
                        onConnect.invoke()
                        break
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                socket.awaitClosed()
            }
        }
    }
}