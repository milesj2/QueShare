package ga.kojin.queshare.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.queshare.R
import ga.kojin.queshare.data.ContactsRepository
import ga.kojin.queshare.ui.main.contactslist.ContactsRecyclerViewAdapter

class FavouritesFragment : Fragment()
{
    private val TAG: String = "FavouritesFragment"

    private lateinit var favouritesRV: RecyclerView
    private lateinit var contactsRepo: ContactsRepository
    private lateinit var layoutNoFavourites: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_favourites, container, false)

        favouritesRV = root.findViewById(R.id.contact_list)
        contactsRepo = ContactsRepository(requireContext())

        val contactsAdapter = ContactsRecyclerViewAdapter(requireContext())

        favouritesRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        layoutNoFavourites = root.findViewById(R.id.layout_no_favourites)

        refreshContacts()

        return root
    }

    fun refreshContacts() {
        if (context == null)
            return

        if (favouritesRV.adapter == null) {
            Log.v(TAG, "No adapter for dataset!")
            return
        }

        val adapter = favouritesRV.adapter as ContactsRecyclerViewAdapter

        adapter.contactsList = contactsRepo.getStarredContacts()

        Log.v(TAG, "notifyDataSetChanged")
        favouritesRV.adapter!!.notifyDataSetChanged()

        if (adapter.contactsList.size > 0) {
            layoutNoFavourites.visibility = View.GONE
        } else {
            layoutNoFavourites.visibility = View.VISIBLE
        }

    }
}