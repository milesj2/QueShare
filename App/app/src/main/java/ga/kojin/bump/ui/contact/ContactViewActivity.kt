package ga.kojin.bump.ui.contact

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.models.SystemContact

class ContactViewActivity : AppCompatActivity() {

    private var contact : SystemContact? = null
    private val contactsRepo : ContactsRepository = ContactsRepository(this)
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var starred : Boolean = false

    private lateinit var imgStarred : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        val btnNavBack : ImageView = findViewById(R.id.imgNavBack)

        btnNavBack.setOnClickListener {
            finish()
        }

        val userID: Int? = intent.getStringExtra("contact")?.toInt()

        if (userID != null) {
            contact = contactsRepo.getContactBySystemID(userID)
        }

        imgStarred = findViewById(R.id.imgStarred)
        imgStarred.setOnClickListener {
            starred = !starred
            setStarredIco()
        }
        starred = contact?.starred == true
        setStarredIco()

        val contactAdapter = ContactAdapter(contact, this, supportFragmentManager)

        viewPager = findViewById(R.id.detailsViewPager)
        tabLayout = findViewById(R.id.detailsTabBar)

        viewPager.adapter = contactAdapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setStarredIco() {
        if (starred){
            imgStarred.setImageResource(android.R.drawable.star_big_on)
        } else {
            imgStarred.setImageResource(android.R.drawable.star_big_off)
        }
    }
}