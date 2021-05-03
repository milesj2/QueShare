package ga.kojin.queshare.helpers.networking

import android.util.Log
import ga.kojin.queshare.data.DBDriver
import ga.kojin.queshare.data.PhotoRepository
import ga.kojin.queshare.helpers.BitmapHelper
import ga.kojin.queshare.models.persisted.Photo
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress

object ServerSocketHelper {

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