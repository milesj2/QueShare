package ga.kojin.bump.ui.favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.ui.contactslist.ContactsRecyclerViewAdapter

class FavouritesFragment : Fragment()
{
    private val TAG : String = "FavouritesFragment"

    private lateinit var favouritesRV: RecyclerView
    private lateinit var contactsRepo : ContactsRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_contacts, container, false)

        favouritesRV = root.findViewById(R.id.contact_list)
        contactsRepo = ContactsRepository(requireContext())

        val contactsAdapter = ContactsRecyclerViewAdapter(requireContext())

        favouritesRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        refreshContacts()

        return root
    }

    fun refreshContacts() {
        if (context == null)
            return

        if (favouritesRV.adapter == null){
            Log.v(TAG, "No adapter for dataset!")
            return
        }

        val adapter = favouritesRV.adapter as ContactsRecyclerViewAdapter

        adapter.contactsList = contactsRepo.getStarredContacts()

        Log.v(TAG, "notifyDataSetChanged")
        favouritesRV.adapter!!.notifyDataSetChanged()
    }


}