package ga.kojin.queshare.ui.share

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.zxing.BarcodeFormat
import ga.kojin.queshare.R
import ga.kojin.queshare.helpers.QRCodeHelper
import java.util.*

class QRScanActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private var scannerInitialised: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scan)

        scannerView = findViewById(R.id.scanner_view)

        val btnNavBack: ImageView = findViewById(R.id.imgNavBack)
        btnNavBack.setOnClickListener {
            finish()
        }

        val btnBump: FloatingActionButton = findViewById(R.id.fabAdd)

        btnBump.setOnClickListener {
            val dialog = Dialog(this, R.style.CustomAlertDialog)

            dialog.setContentView(R.layout.dialog_qr_code)

            val qrCode: ImageView = dialog.findViewById(R.id.imgQRCode)

            val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
            val port = Random().nextInt(9999 - 1000) + 1000
            val width: Int = Resources.getSystem().displayMetrics.widthPixels
            qrCode.setImageBitmap(
                QRCodeHelper().generateQRCode(
                    width,
                    width,
                    "bump://ga.kojin.bump/share?host=$ip&port=$port&key=1234"
                )
            )
            dialog.show()
        }

        startQRScanner()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == 0) {
            startQRScanner()
        }
    }

    private fun startQRScanner() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            return
        }

        codeScanner = CodeScanner(this, scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val intent = Intent("ga.kojin.bump.ui.share.Network")
                intent.data = Uri.parse(it.text)
                intent.setClass(this, ClientNetworkShare::class.java)
                this.startActivity(intent)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerInitialised = true

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        if (scannerInitialised) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (scannerInitialised) {
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }
}