package ga.kojin.bump.ui.bump

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import ga.kojin.bump.R
import ga.kojin.bump.ui.bump.qrshare.QRShareActivity

class BumpActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView
    private var scannerInitialised: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bump)

        scannerView = findViewById(R.id.scanner_view)

        val btnNavBack : ImageView = findViewById(R.id.imgNavBack)
        btnNavBack.setOnClickListener {
            finish()
        }

        val btnBump : Button = findViewById(R.id.btnShare)

        btnBump.setOnClickListener {
            val intent = Intent("ga.kojin.bump.ui.bump.qrshare.QRShareActivity")
            intent.setClass(applicationContext, QRShareActivity::class.java)
            this.startActivity(intent)
        }

        startQRScanner()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == 0){
            startQRScanner()
        }
    }

    private fun startQRScanner() {
        if (applicationContext.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            return
        }


        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                bump()
                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerInitialised = true

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        if (scannerInitialised){
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (scannerInitialised){
            codeScanner.releaseResources()
        }
        super.onPause()
    }

    private fun bump() {}


}