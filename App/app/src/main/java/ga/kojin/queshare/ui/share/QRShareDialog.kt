package ga.kojin.queshare.ui.share

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ga.kojin.queshare.R
import ga.kojin.queshare.helpers.QRCodeHelper
import ga.kojin.queshare.helpers.networking.ServerSocketHelper
import io.ktor.network.sockets.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class QRShareDialog(context: Context) : Dialog(context, R.style.CustomAlertDialog) {

    private val TAG: String = "QRShareDialog"
    val socket = ServerSocketHelper
    lateinit var connection: Socket
    private var launchedShare = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_qr_code)

        val qrCode: ImageView = findViewById(R.id.imgQRCode)

        val wm = context.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
        val port = Random().nextInt(9999 - 1000) + 1000

        val width: Int = Resources.getSystem().displayMetrics.widthPixels

        val key = "ABC123"

        qrCode.setImageBitmap(
            QRCodeHelper().generateQRCode(
                width,
                width,
                "bump://ga.kojin.bump/share?host=$ip&port=$port&key=$key"
            )
        )

        GlobalScope.launch {
            suspend {
                socket.setupSocket(ip, port, key) {
                    //Log.v(TAG, "New Connection from: ${connection.remoteAddress}")
                    launchedShare = true
                    val intent = Intent("ga.kojin.bump.ui.share.HostNetworkShare")
                    intent.setClass(context, HostNetworkShare::class.java)
                    context.startActivity(intent)
                    dismiss()
                }
            }.invoke()
        }

    }

}