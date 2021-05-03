package ga.kojin.bump

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import ga.kojin.bump.databinding.ActivityMainBinding
import ga.kojin.bump.helpers.QRCodeHelper
import ga.kojin.bump.ui.share.QRScanActivity
import ga.kojin.bump.ui.main.SectionsPagerAdapter
import ga.kojin.bump.ui.share.qrshare.QRShareDialog
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    private var width: Int = Resources.getSystem().displayMetrics.widthPixels

    private val TAG: String = "MainActivity"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        viewPager.offscreenPageLimit = 3

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val fabAdd: FloatingActionButton = binding.fabAdd

        fabAdd.setOnClickListener { view ->
            val intent = Intent("ga.kojin.bump.ui.contact.BumpActivity")
            intent.setClass(this, QRScanActivity::class.java)
            this.startActivity(intent)
        }

        val fabShare: FloatingActionButton = binding.fabShare

        fabShare.setOnClickListener {
            val dialog = QRShareDialog(this)
            dialog.show()
        }

        requireContacts()
    }

    override fun onResume() {
        super.onResume()
        sectionsPagerAdapter.refreshData()
    }

    private fun requireContacts() {
        Log.v(TAG, "Requiring Contacts")
        requestPermission(Manifest.permission.READ_CONTACTS, 100)
    }

    private fun requestPermission(permission: String, dReturn: Int) {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), dReturn)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.v(TAG, "Permission result for ${permissions[0]} is ${grantResults[0]}")

        if (grantResults[0] == 0) {
            sectionsPagerAdapter.refreshData()
        }
    }

}