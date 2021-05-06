package ga.kojin.queshare.helpers.networking

import android.content.Context
import android.util.Log
import ga.kojin.queshare.helpers.BitmapHelper
import ga.kojin.queshare.models.persisted.Photo
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress
import java.nio.channels.ClosedChannelException

object ServerSocketHelper {

    var connection: Socket? = null
    var input: ByteReadChannel? = null
    private var serverSocket: ServerSocket? = null

    private const val TAG: String = "ServerSocket"

    fun setupSocket(url: String, port: Int) {
        Log.v(TAG, "Starting Server...")
        serverSocket = aSocket(ActorSelectorManager(Dispatchers.IO)).tcp()
            .bind(InetSocketAddress(url, port))
    }

    suspend fun listen(key: String, onConnect: () -> Unit) {
        Log.v(TAG, connection.toString())
        connection?.dispose()
        connection = null
        while (connection == null) {
            Log.v(TAG, "Listening")
            val socket = serverSocket?.accept()

            Log.v(TAG, "Found new connection '${socket?.remoteAddress}'.")
            input = socket?.openReadChannel()
            Log.i(TAG, "Waiting for key...")
            var line: String = ""

            while (line == "") {
                line = input!!.readUTF8Line().toString()
            }
            Log.i(TAG, "Received input '$line'")
            if (line == key) {
                connection = socket
                onConnect.invoke()
                break
            }
        }
    }

    fun closeServer() {
        try {
            if (serverSocket?.isClosed == false) {
                connection?.close()
                //serverSocket?.close()
            }
        } catch (e: ClosedChannelException) {
            Log.e(TAG, "Closed Channel Exception")
        } catch (e: Exception) {
            Log.e(TAG, "Closed Channel Exception")
        }
    }

    suspend fun sendPhoto(output: ByteWriteChannel, photo: Photo?) {
        if (photo != null) {
            val byteArray = BitmapHelper.compressBitmapToStream(photo.bitmap, 50).toByteArray()
            output.writeStringUtf8("${byteArray.size}\r\n")
            output.writeFully(byteArray, 0, byteArray.size)
        } else {
            output.writeStringUtf8("NOPHOTO\r\n")
        }
    }


}