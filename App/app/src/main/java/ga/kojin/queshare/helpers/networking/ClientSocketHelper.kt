package ga.kojin.queshare.helpers.networking

import android.util.Log
import io.ktor.network.sockets.*
import io.ktor.utils.io.*

object ClientSocketHelper {

    private val TAG: String = "ClientSocket"

    suspend fun receiveFile(input: ByteReadChannel, socket: Socket): ByteArray? {
        var fileResponse: ByteArray? = null
        var fileSize = ""
        while (fileSize == "") {
            fileSize = input.readUTF8Line().toString()
        }
        Log.v(TAG, "'$fileSize'")
        if (fileSize != "NOPHOTO") {
            fileResponse = ByteArray(fileSize.toInt())
            input.readFully(fileResponse, 0, fileSize.toInt())
        }
        return fileResponse
    }

}