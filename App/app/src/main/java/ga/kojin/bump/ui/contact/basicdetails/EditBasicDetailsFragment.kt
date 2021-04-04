package ga.kojin.bump.ui.contact.basicdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import ga.kojin.bump.R
import ga.kojin.bump.models.SystemContact
import org.w3c.dom.Text

class EditBasicDetailsFragment (val contact: SystemContact?) : Fragment() {

    lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        root = inflater.inflate(R.layout.fragment_edit_basic_details, container, false)

        if (contact != null) {
            populateFields()
        }
        return root
    }

    private fun populateFields() {
        val contactName : EditText = root.findViewById(R.id.txtName)
        val contactNumber : EditText = root.findViewById(R.id.txtNumber)

        contactName.setText(contact!!.name)
        contactNumber.setText(contact.number)
    }
}