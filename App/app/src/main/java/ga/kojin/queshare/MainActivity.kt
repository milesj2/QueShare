package ga.kojin.queshare

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import ga.kojin.queshare.databinding.ActivityMainBinding
import ga.kojin.queshare.helpers.PermissionsHelper
import ga.kojin.queshare.helpers.QueShareDialogHelper
import ga.kojin.queshare.helpers.SharedPreferences
import ga.kojin.queshare.ui.share.QRScanActivity
import ga.kojin.queshare.ui.main.MainTabAdapter
import ga.kojin.queshare.ui.share.QRShareDialog


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
    private lateinit var mainTabAdapter: MainTabAdapter
    private var width: Int = Resources.getSystem().displayMetrics.widthPixels
    private var QRDialog: QRShareDialog? = null

    private lateinit var userPreferences: SharedPreferences

    private val TAG: String = "MainActivity"

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = SharedPreferences(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        viewPager.offscreenPageLimit = 3

        mainTabAdapter = MainTabAdapter(this, supportFragmentManager)
        viewPager.adapter = mainTabAdapter
        viewPager.currentItem = 1

        val tabs: TabLayout = binding.tabs
        val fabAdd: FloatingActionButton = binding.fabAdd
        val fabShare: FloatingActionButton = binding.fabShare

        tabs.setupWithViewPager(viewPager)

        fabAdd.setOnClickListener { view ->
            if (userPreferences.getValueBoolean(userPreferences.PROFILE_SET_UP, false)) {
                val intent = Intent("ga.kojin.queshare.ui.share.QRShareDialog")
                intent.setClass(this, QRScanActivity::class.java)
                this.startActivity(intent)
            } else {
                QueShareDialogHelper.showAlertDialog(
                    this, "Sorry!",
                    "Please setup your profile to use this feature.",
                    "Ok"
                )
            }
        }

        fabShare.setOnClickListener {
            if (QRDialog?.isShowing == true) {
                return@setOnClickListener
            }

            if (userPreferences.getValueBoolean(userPreferences.PROFILE_SET_UP, false)) {
                QRDialog = QRShareDialog(this)
                QRDialog?.show()
            } else {
                QueShareDialogHelper.showAlertDialog(
                    this, "Sorry!",
                    "Please setup your profile to use this feature.",
                    "Ok"
                )
            }
        }

        requireContacts()
    }

    override fun onResume() {
        super.onResume()
        mainTabAdapter.refreshData()

    }

    private fun requireContacts() {
        Log.v(TAG, "Requiring Contacts")
        // PermissionsHelper.requestPermission(this, Manifest.permission.READ_CONTACTS, 100)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.v(TAG, "Permission result for ${permissions[0]} is ${grantResults[0]}")

        if (grantResults[0] == 0) {
            mainTabAdapter.refreshData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}