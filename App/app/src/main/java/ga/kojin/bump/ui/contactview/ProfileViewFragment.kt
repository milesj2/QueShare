package ga.kojin.bump.ui.contactview

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.data.PhotoRepository
import ga.kojin.bump.models.persisted.Contact
import ga.kojin.bump.models.persisted.Photo


class ProfileViewFragment : Fragment() {

    private lateinit var contact: Contact
    private lateinit var contactsRepo: ContactsRepository
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var editMode: Boolean = false
    private lateinit var contactAdapter: ContactViewAdapter
    private lateinit var avatar: ImageView

    private val pickImage = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_profile_view, container, false)

        contactsRepo = ContactsRepository(root.context)
        contact = contactsRepo.getUserProfile()


        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        val starred: ImageView = root.findViewById(R.id.imgStarred)
        // val title: TextView = root.findViewById(R.id.txtName)
        val navBack: ImageView = root.findViewById(R.id.imgNavBack)
        avatar = root.findViewById(R.id.imageView)

        // toolbar.visibility = View.GONE
        starred.visibility = View.GONE
        //title.visibility = View.GONE
        navBack.visibility = View.GONE

        val photo = PhotoRepository(requireContext()).getImageByContactID(contact.id)?.bitmap
        if (photo == null) {
            avatar.setImageResource(R.mipmap.ic_default_avatar)
        } else {
            avatar.setImageBitmap(photo)
        }

        contactAdapter = ContactViewAdapter(contact, requireContext(), childFragmentManager)

        viewPager = root.findViewById(R.id.detailsViewPager)
        tabLayout = root.findViewById(R.id.detailsTabBar)

        viewPager.adapter = contactAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val btnEdit: ImageView = root.findViewById(R.id.imgEdit)
        val btnDone: ImageView = root.findViewById(R.id.imgDone)
        val btnClear: ImageView = root.findViewById(R.id.imgClear)
        val btnDelete: ImageView = root.findViewById(R.id.imgDelete)
        val btnAddImage: ImageView = root.findViewById(R.id.imgAddImage)

        btnDelete.visibility = View.GONE

        btnEdit.setOnClickListener {
            editMode = true
            btnEdit.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            btnClear.visibility = View.VISIBLE
            contactAdapter.setEdit(editMode)
        }

        btnDone.setOnClickListener {
            saveProfile()
            editMode = false
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactAdapter.setEdit(editMode)
        }

        btnClear.setOnClickListener {
            editMode = false
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactAdapter.setEdit(editMode)
        }

        btnAddImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        return root
    }

    private fun saveProfile() {
        contactAdapter.saveDetails(false)
    }

    override fun onResume() {
        super.onResume()
        // ContactViewAdapter(contact, this.requireContext(), requireFragmentManager())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            val imageUri = data?.data
            avatar.setImageURI(imageUri)
            val bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            val photo = Photo(-1, contact.id, bitmap)
            PhotoRepository(requireContext()).upsertContactImage(photo)
        }
    }

}