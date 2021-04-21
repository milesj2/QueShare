package ga.kojin.bump.ui.contact.basicdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.models.persisted.Contact

class BasicDetailsFragment(var contact: Contact) : Fragment() {

    lateinit var root : View
    lateinit var viewLayout : LinearLayout
    lateinit var editLayout: LinearLayout
    private lateinit var contactNameView : TextView
    private lateinit var contactNumberView : TextView
    lateinit var contactNameEdit : EditText
    lateinit var contactNumberEdit : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_basic_details, container, false)

        viewLayout = root.findViewById(R.id.layoutView)
        editLayout = root.findViewById(R.id.layoutEdit)

        contactNameView = root.findViewById(R.id.txtName)
        contactNumberView = root.findViewById(R.id.txtNumber)
        contactNameEdit = root.findViewById(R.id.txtNameEdit)
        contactNumberEdit = root.findViewById(R.id.txtNumberEdit)

        editLayout.visibility = View.GONE


        populateFields()


        return root
    }

    private fun populateFields() {
        contactNameView.text = contact.name
        contactNumberView.text = contact.number
        contactNameEdit.setText(contact.name)
        contactNumberEdit.setText(contact.number)

    }

    fun toggleEditMode(editMode: Boolean){
        if (editMode){
            viewLayout.visibility = View.GONE
            editLayout.visibility = View.VISIBLE
        } else {
            viewLayout.visibility = View.VISIBLE
            editLayout.visibility = View.GONE
            populateFields()
        }
    }

    fun saveDetails(starred: Boolean) {
        contact.starred = starred
        contact.name = contactNameEdit.text.toString()
        contact.number = contactNumberEdit.text.toString()

        ContactsRepository(requireContext()).updateContact(contact)
        populateFields()
    }

}