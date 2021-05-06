package ga.kojin.queshare.ui.contactview

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.google.android.material.tabs.TabLayout
import ga.kojin.queshare.R
import ga.kojin.queshare.data.ContactsRepository
import ga.kojin.queshare.data.PhotoRepository
import ga.kojin.queshare.helpers.*
import ga.kojin.queshare.models.persisted.Contact
import ga.kojin.queshare.models.persisted.Photo


class ProfileViewFragment : Fragment() {

    private lateinit var contact: Contact
    private lateinit var contactsRepo: ContactsRepository
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var editMode: Boolean = false
    private lateinit var contactAdapter: ContactViewAdapter
    private lateinit var avatar: ImageView
    private lateinit var blurredBackground: ImageView
    private lateinit var root: View
    private lateinit var view2: CardView
    private lateinit var btnEdit: ImageView
    private lateinit var btnDelete: ImageView
    private lateinit var btnDone: ImageView
    private lateinit var btnClear: ImageView

    private val pickImage = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        root = inflater.inflate(R.layout.fragment_profile_view, container, false)

        contactsRepo = ContactsRepository(root.context)
        contact = contactsRepo.getUserProfile()


        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        val starred: ImageView = root.findViewById(R.id.imgStarred)
        val navBack: ImageView = root.findViewById(R.id.imgNavBack)
        val imageConstraint: ConstraintLayout = root.findViewById(R.id.imageConstraint)
        avatar = root.findViewById(R.id.imageView)
        blurredBackground = root.findViewById(R.id.imgBlurredBackground)
        view2 = root.findViewById(R.id.view2)

        // toolbar.visibility = View.GONE
        starred.visibility = View.GONE
        //title.visibility = View.GONE
        navBack.visibility = View.GONE


        val photo = PhotoRepository(requireContext()).getImageByContactID(contact.id)?.bitmap
        if (photo == null) {
            val txtInitial: TextView = root.findViewById(R.id.txtInitial)
            if (contact.name.isNotEmpty()) {
                txtInitial.text = "${contact.name[0]}"
            }
            txtInitial.visibility = View.VISIBLE
        } else {
            setProfilePhoto(photo)

            view2.visibility = View.VISIBLE
        }

        contactAdapter = ContactViewAdapter(requireContext(), childFragmentManager)
        setContact()

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

        btnEdit = root.findViewById(R.id.imgEdit)
        btnDelete = root.findViewById(R.id.imgDelete)
        btnDone = root.findViewById(R.id.imgDone)
        btnClear = root.findViewById(R.id.imgClear)
        val btnAddImage: ImageView = root.findViewById(R.id.imgAddImage)

        btnDelete.visibility = View.GONE

        btnEdit.setOnClickListener {
            editMode = true
            btnEdit.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            btnClear.visibility = View.VISIBLE
            contactAdapter.setEdit(editMode)
            AnimationHelper.animateButtons(requireContext(), btnEdit, btnDelete, btnClear, btnDone)
        }

        btnDone.setOnClickListener {
            if (!saveProfile()) {
                QueShareDialogHelper.showAlertDialog(
                    requireContext(),
                    "Cannot save contact",
                    "Name cannot be blank.",
                    "Ok"
                )
            } else {
                val sharedPreferences = SharedPreferences(requireContext())
                sharedPreferences.save(sharedPreferences.PROFILE_SET_UP, true)
            }
            editMode = false
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactAdapter.setEdit(editMode)
            AnimationHelper.animateButtons(requireContext(), btnDone, btnClear, btnDelete, btnEdit)
        }

        btnClear.setOnClickListener {
            editMode = false
            btnEdit.visibility = View.VISIBLE
            btnDone.visibility = View.GONE
            btnClear.visibility = View.GONE
            contactAdapter.setEdit(editMode)
            AnimationHelper.animateButtons(requireContext(), btnDone, btnClear, btnDelete, btnEdit)
        }

        btnAddImage.setOnClickListener {
            PermissionsHelper.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                pickImage
            )
        }
        return root
    }

    private fun saveProfile(): Boolean = contactAdapter.saveDetails(false)

    private fun setProfilePhoto(photo: Bitmap) {
        avatar.setImageBitmap(photo)
        view2.visibility = View.VISIBLE
        blurredBackground.setImageBitmap(
            BitmapHelper.blurBitmap(
                requireContext(),
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

    private fun showImagePicDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Pick Image From")
        builder.setItems(options) { _, which ->
            if (which == 0) {
                pickFromGallery()
            } else if (which == 1) {
                pickFromGallery()
            }
        }
        builder.create().show()
    }

    private fun pickFromGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setFixAspectRatio(true)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setActivityTitle("Select Crop")
            .start(requireContext(), this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == pickImage) {
            pickFromGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result?.uriContent
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, resultUri)
                setProfilePhoto(bitmap)
                val photo = Photo(-1, contact.id, bitmap)
                PhotoRepository(requireContext()).upsertContactImage(photo)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setContact()
    }

    private fun setContact() {
        contactAdapter.setContact(contact)
    }
}