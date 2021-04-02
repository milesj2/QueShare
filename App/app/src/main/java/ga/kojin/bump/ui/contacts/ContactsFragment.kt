package ga.kojin.bump.ui.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.helpers.SystemContactsHelper
import ga.kojin.bump.models.SystemContact
import ga.kojin.bump.ui.contactslist.ContactsRVAdapter

class ContactsFragment : Fragment()
{

    private lateinit var contactsList: ArrayList<SystemContact>
    private lateinit var contactsRV: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsRV = root.findViewById(R.id.contact_list)

        contactsList = SystemContactsHelper.getContactList(requireContext())!!

        val contactsAdapter =  ContactsRVAdapter(requireContext(), contactsList)

        contactsRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        return root
    }

}