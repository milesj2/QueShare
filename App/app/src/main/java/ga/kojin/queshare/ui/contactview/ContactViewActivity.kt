package ga.kojin.queshare.ui.contactview

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.textfield.TextInputEditText
import ga.kojin.queshare.R
import ga.kojin.queshare.data.ContactsRepository
import ga.kojin.queshare.data.PhotoRepository
import ga.kojin.queshare.helpers.BitmapHelper
import ga.kojin.queshare.helpers.DimensionsHelper
import ga.kojin.queshare.models.persisted.Contact

class ContactViewActivity : AppCompatActivity() {

    private lateinit var contact: Contact
    private val contactsRepo: ContactsRepository = ContactsRepository(this)
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var starred: Boolean = false
    private var editMode: Boolean = false
    private lateinit var contactViewAdapter: ContactViewAdapter
    private lateinit var avatar: ImageView
    private lateinit var blurredBackground: ImageView

    private lateinit var txtName: TextInputEditText

    private lateinit var imgStarred: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_contact)

        val btnNavBack: ImageView = findViewById(R.id.imgNavBack)

        btnNavBack.setOnClickListener {
            finish()
        }

        val userID: Long = (intent.extras ?: return).getLong("Contact")
        contact = (contactsRepo.getContactByID(userID) ?: return)

        viewPager = findViewById(R.id.detailsViewPager)
        tabLayout = findViewById(R.id.detailsTabBar)
        imgStarred = findViewById(R.id.imgStarred)
        avatar = findViewById(R.id.imageView)
        blurredBackground = findViewById(R.id.imgBlurredBackground)


        val photo = PhotoRepository(this).getImageByContactID(contact.id)?.bitmap
        if (photo == null) {
            val txtInitial: TextView = findViewById(R.id.txtInitial)
            txtInitial.text = "${contact.name[0]}"
            txtInitial.visibility = View.VISIBLE
        } else {
            setProfilePhoto(photo)
            val view2: CardView = findViewById(R.id.view2)
            view2.visibility = View.VISIBLE
        }

        imgStarred.setOnClickListener {
            starred = !starred
            imgStarred.isSelected = starred
            contactsRepo.setContactFavouriteStatus(userID, starred)
            setStarredIco()
        }

        starred = contact.starred
        setStarredIco()

        contactViewAdapter = ContactViewAdapter(this, supportFragmentManager)
        contactViewAdapter.setContact(contact)

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
        val btnAddImage: ImageView = findViewById(R.id.imgAddImage)

        btnAddImage.visibility = View.GONE

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
            btnDelete.visibility = View.VISIBLE
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactViewAdapter.setEdit(editMode)
        }

        btnClear.setOnClickListener {
            editMode = false
            btnDelete.visibility = View.VISIBLE
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactViewAdapter.setEdit(editMode)
        }

        btnDelete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(viewPager.context)

            dialogBuilder.setMessage("Are you sure you wish to proceed?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    contactsRepo.deleteContact(contact.id)
                    finish()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.cancel()
                }

            val alert = dialogBuilder.create()
            alert.setTitle("Deleting contact")
            alert.show()
        }
    }

    private fun setStarredIco() {
        if (starred) {
            //imgStarred.setImageResource(android.R.drawable.star_big_on)
        } else {
            //imgStarred.setImageResource(android.R.drawable.star_big_off)
        }
    }

    private fun saveContact() {
        contactViewAdapter.saveDetails(starred)
    }

    private fun setProfilePhoto(photo: Bitmap) {
        avatar.setImageBitmap(photo)
        blurredBackground.setImageBitmap(
            BitmapHelper.blurRenderScript(
                this,
                BitmapHelper.resizeBitmap(
                    photo,
                    DimensionsHelper.getWindowWidth(),
                    DimensionsHelper.dpToPx(200F).toInt(),
                    false,
                    true
                ),
                25
            )
        )
    }


}