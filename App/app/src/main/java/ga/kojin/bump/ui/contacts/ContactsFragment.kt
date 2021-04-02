package ga.kojin.bump.ui.contacts

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.helpers.SystemContactsHelper
import ga.kojin.bump.models.SystemContact
import ga.kojin.bump.ui.contactslist.ContactsRVAdapter
import java.util.jar.Manifest

class ContactsFragment : Fragment()
{
    private val TAG : String = "ContactsFragment"

    private lateinit var contactsRV: RecyclerView
    private lateinit var contactsRepo : ContactsRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsRV = root.findViewById(R.id.contact_list)
        contactsRepo = ContactsRepository(requireContext())

        val contactsAdapter = ContactsRVAdapter(requireContext())

        contactsRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        refreshContacts()

        return root
    }

    fun refreshContacts() {
        if (context == null)
            return

        if (contactsRV.adapter == null){
            Log.v(TAG, "No adapter for dataset!")
            return
        }

        val adapter = contactsRV.adapter as ContactsRVAdapter

        adapter.contactsList = contactsRepo.getUsers()

        if(adapter.contactsList.size == 0){
            Log.v(TAG, "No stored users.")
            requestImport()
        }

        Log.v(TAG, "notifyDataSetChanged")
        this.contactsRV.adapter!!.notifyDataSetChanged()
    }

    private fun requestImport() {

        if (context?.checkSelfPermission(android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED){
            return
        }

        val dialogBuilder = AlertDialog.Builder(requireContext())

        // set message of alert dialog
        dialogBuilder.setMessage("There are no contacts, would you like to import from system?")
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Proceed") { _, _ ->
                importContacts()
            }
            // negative button text and action
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("No Contacts Found!")
        // show alert dialog
        alert.show()
    }

    private fun importContacts() {
        Log.v(TAG, "Importing contacts")

        val contacts = SystemContactsHelper.getContactList(requireContext())

        contacts?.forEach{
            contactsRepo.addUser(it)
        }

        refreshContacts()

    }

}