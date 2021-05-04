package ga.kojin.queshare.ui.contactview.basicdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import ga.kojin.queshare.R
import ga.kojin.queshare.data.ContactsRepository
import ga.kojin.queshare.models.persisted.Contact

class BasicDetailsFragment : Fragment() {

    private val TAG: String = "BasicDetailsFragment"

    var contact: Contact? = null
    lateinit var root: View
    var viewLayout: LinearLayout? = null
    var editLayout: LinearLayout? = null
    private lateinit var contactNameView: TextView
    private lateinit var contactNumberView: TextView
    lateinit var contactNameEdit: EditText
    lateinit var contactNumberEdit: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.w(TAG, "CREATED ${this}")
        val view2: View = inflater.inflate(R.layout.fragment_basic_details, container, false)

        viewLayout = view2.findViewById(R.id.layoutView)
        editLayout = view2.findViewById(R.id.layoutEdit)

        contactNameView = view2.findViewById(R.id.txtName)
        contactNumberView = view2.findViewById(R.id.txtNumber)
        contactNameEdit = view2.findViewById(R.id.txtNameEdit)
        contactNumberEdit = view2.findViewById(R.id.txtNumberEdit)

        editLayout?.visibility = View.GONE

        populateFields()
        root = view2
        return view2
    }

    private fun populateFields() {
        contactNameView.text = (contact ?: return).name
        contactNumberView.text = (contact ?: return).number
        contactNameEdit.setText((contact ?: return).name)
        contactNumberEdit.setText((contact ?: return).number)

    }

    override fun onResume() {
        super.onResume()
        Log.w(TAG, "RESUMED ${this}")

        populateFields()


    }

    override fun onStart() {
        super.onStart()
        Log.w(TAG, "ONSTART ${this} ")
    }

    override fun onPause() {
        super.onPause()
        Log.w(TAG, "PAUSED ${this}")
    }

    fun toggleEditMode(editMode: Boolean) {
        if (editMode) {
            viewLayout?.visibility = View.GONE
            editLayout?.visibility = View.VISIBLE
        } else {
            viewLayout?.visibility = View.VISIBLE
            editLayout?.visibility = View.GONE
            populateFields()
        }
    }

    fun saveDetails(starred: Boolean): Boolean {
        try {
            if (contact == null) return false
            contact?.starred = starred
            contact?.name = contactNameEdit.text.toString()
            contact?.number = contactNumberEdit.text.toString()

            if (contact?.name.isNullOrEmpty() || contact?.name.isNullOrBlank())
                return false

            contact?.let { ContactsRepository(requireContext()).updateContact(it) }
            populateFields()
            return true
        } catch (e: UninitializedPropertyAccessException) {
            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(TAG, "DESTROYED ${this}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.w(TAG, "DESTROYEDVIEW ${this}")
    }
}