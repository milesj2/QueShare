package ga.kojin.bump.ui.contactview

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.textfield.TextInputEditText
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.models.persisted.Contact

class ContactViewActivity : AppCompatActivity() {

    private lateinit var contact: Contact
    private val contactsRepo: ContactsRepository = ContactsRepository(this)
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var starred: Boolean = false
    private var editMode: Boolean = false
    private lateinit var contactViewAdapter: ContactViewAdapter

    private lateinit var txtName: TextInputEditText

    private lateinit var imgStarred: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_contact_details)

        val btnNavBack: ImageView = findViewById(R.id.imgNavBack)

        btnNavBack.setOnClickListener {
            finish()
        }

        val userID: Long = (intent.extras ?: return).getLong("Contact")

        contact = (contactsRepo.getContactByID(userID) ?: return)

        imgStarred = findViewById(R.id.imgStarred)
        imgStarred.setOnClickListener {
            starred = !starred
            contactsRepo.setContactFavouriteStatus(userID, starred)
            setStarredIco()
        }
        starred = contact.starred == true
        setStarredIco()

        contactViewAdapter = ContactViewAdapter(contact, this, supportFragmentManager)

        viewPager = findViewById(R.id.detailsViewPager)
        tabLayout = findViewById(R.id.detailsTabBar)

        viewPager.adapter = contactViewAdapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val btnEdit: ImageView = findViewById(R.id.imgEdit)
        val btnDone: ImageView = findViewById(R.id.imgDone)
        val btnClear: ImageView = findViewById(R.id.imgClear)
        val btnDelete: ImageView = findViewById(R.id.imgDelete)
        val txtInitial: TextView = findViewById(R.id.txtInitial)

        txtInitial.text = "${contact.name[0]}"
        txtInitial.visibility = View.VISIBLE

        btnEdit.setOnClickListener {
            editMode = true
            btnEdit.visibility = View.GONE
            btnDelete.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            btnClear.visibility = View.VISIBLE
            contactViewAdapter.setEdit(editMode)
        }

        btnDone.setOnClickListener {
            saveContact()
            editMode = false
            btnEdit.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactViewAdapter.setEdit(editMode)
        }

        btnClear.setOnClickListener {
            editMode = false
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactViewAdapter.setEdit(editMode)
        }

        btnDelete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(viewPager.context)

            dialogBuilder.setMessage("There are no contacts, would you like to import from system?")
                .setCancelable(false)
                .setPositiveButton("Proceed") { _, _ ->
                    contactsRepo.deleteContact(contact.id)
                    this.finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val alert = dialogBuilder.create()
            alert.setTitle("Are you sure?")
            alert.show()
        }
    }

    private fun setStarredIco() {
        if (starred) {
            imgStarred.setImageResource(android.R.drawable.star_big_on)
        } else {
            imgStarred.setImageResource(android.R.drawable.star_big_off)
        }
    }

    private fun saveContact() {
        contactViewAdapter.saveDetails(starred)
    }
}