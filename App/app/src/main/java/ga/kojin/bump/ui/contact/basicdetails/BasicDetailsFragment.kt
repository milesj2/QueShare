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
import ga.kojin.bump.models.SystemContact
import org.w3c.dom.Text

class BasicDetailsFragment(val contact: SystemContact?) : Fragment() {

    lateinit var root : View
    lateinit var viewLayout : LinearLayout
    lateinit var editLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        root = inflater.inflate(R.layout.fragment_basic_details, container, false)

        viewLayout = root.findViewById(R.id.layoutView)
        editLayout = root.findViewById(R.id.layoutEdit)

        editLayout.visibility = View.GONE

        if (contact != null) {
            populateFields()
        }

        return root
    }

    private fun populateFields() {
        val contactNameView : TextView = root.findViewById(R.id.txtName)
        val contactNumberView : TextView = root.findViewById(R.id.txtNumber)
        val contactNameEdit : EditText = root.findViewById(R.id.txtNameEdit)
        val contactNumberEdit : EditText = root.findViewById(R.id.txtNumberEdit)

        contactNameView.text = contact!!.name
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
        }
    }

}