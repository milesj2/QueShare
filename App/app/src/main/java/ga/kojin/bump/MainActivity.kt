package ga.kojin.bump

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ga.kojin.bump.ui.main.SectionsPagerAdapter
import ga.kojin.bump.databinding.ActivityMainBinding
import ga.kojin.bump.ui.bump.BumpActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            val intent = Intent("ga.kojin.bump.ui.contact.BumpActivity")
            intent.setClass(this, BumpActivity::class.java)
            this.startActivity(intent)
        }

        requireContacts()

    }

    private fun requireContacts() {
        Log.v(TAG, "Requiring Contacts")
        requestPermission(Manifest.permission.READ_CONTACTS, 100)
    }

    private fun requestPermission(permission : String, dReturn : Int){
        if (applicationContext.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED){
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

        if (grantResults[0] == 0){
            sectionsPagerAdapter.contactsFragment.refreshContacts()
        }
    }

}