package ga.kojin.queshare.ui.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import ga.kojin.queshare.R
import ga.kojin.queshare.data.ContactsRepository
import ga.kojin.queshare.helpers.SystemContactsHelper
import ga.kojin.queshare.models.persisted.Contact
import ga.kojin.queshare.ui.main.contactslist.ContactsRecyclerViewAdapter

class ContactsFragment : Fragment() {
    private val TAG: String = "ContactsFragment"

    private lateinit var contactsRV: RecyclerView
    private lateinit var contactsRepo: ContactsRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        contactsRV = root.findViewById(R.id.contact_list)
        contactsRepo = ContactsRepository(requireContext())

        val contactsAdapter = ContactsRecyclerViewAdapter(requireContext())

        contactsRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
        }

        refreshContacts()

        val fastScrollerView: FastScrollerView = root.findViewById(R.id.fastscroller)
        val fastscrollerThumbView: FastScrollerThumbView =
            root.findViewById(R.id.fastscroller_thumb)

        fastScrollerView.setupWithRecyclerView(
            contactsRV,
            { position ->
                val item = contactsAdapter.contactsList[position]
                item.name[0].toUpperCase().run {
                    FastScrollItemIndicator.Text("$this")
                }
            }
        )
        fastscrollerThumbView.setupWithFastScroller(fastScrollerView)

        return root
    }

    fun refreshContacts() {
        if (context == null)
            return

        if (contactsRV.adapter == null) {
            Log.v(TAG, "No adapter for dataset!")
            return
        }

        val adapter = contactsRV.adapter as ContactsRecyclerViewAdapter

        adapter.contactsList = contactsRepo.getUsers()

        adapter.contactsList.sortBy { selector(it) }

        if (adapter.contactsList.size == 0) {
            Log.v(TAG, "No stored users.")
            // requestImport()
        }

        Log.v(TAG, "notifyDataSetChanged")
        contactsRV.adapter!!.notifyDataSetChanged()
    }

    fun selector(c: Contact): String = c.name

    private fun requestImport() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            return
        }

        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setMessage("There are no contacts, would you like to import from system?")
            .setCancelable(false)
            .setPositiveButton("Proceed") { _, _ ->
                importContacts()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("No Contacts Found!")
        alert.show()
    }

    private fun importContacts() {
        Log.v(TAG, "Importing contacts")

        val contacts = SystemContactsHelper.getContactList(requireContext())

        contacts?.forEach {
            contactsRepo.addContact(
                Contact(
                    -1,
                    it.name,
                    it.starred,
                    it.number
                )
            )
        }

        refreshContacts()

    }

}