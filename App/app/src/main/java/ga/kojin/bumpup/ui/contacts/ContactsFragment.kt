package ga.kojin.bumpup.ui.contacts

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import ga.kojin.bumpup.R
import ga.kojin.bumpup.adapters.ContactsAdapter
import ga.kojin.bumpup.data.ContactsRepository
import ga.kojin.bumpup.interfaces.IContactsInterface
import ga.kojin.bumpup.models.SystemContact
import java.util.*
import kotlin.collections.ArrayList

class ContactsFragment : Fragment(), IContactsInterface {

    private lateinit var homeViewModel: ContactsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactsList: ArrayList<SystemContact>
    public lateinit var button : FloatingActionButton

    override fun contactsInterface(size: Int) {
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun refreshContacts() {
        if (context == null)
            return
        val contactsRepo = ContactsRepository(requireContext())
        contactsList = contactsRepo.getUsers()
        this.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle? ): View?
    {

        val contactsRepo = ContactsRepository(requireContext())
        homeViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {

        })


        recyclerView = root.findViewById(R.id.contact_list)

        button = root.findViewById(R.id.fbtnBump)

        contactsList = contactsRepo.getUsers()

        val contactsAdapter =  ContactsAdapter(this, requireContext(), contactsList)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        val fastScrollerView: FastScrollerView = root.findViewById(R.id.fastscroller)
        val fastscrollerThumbView: FastScrollerThumbView = root.findViewById(R.id.fastscroller_thumb)

        fastScrollerView.setupWithRecyclerView(recyclerView,
            { position ->
                val item = contactsList?.get(position) // Get your model object
                // or fetch the section at [position] from your database
                item?.name?.substring(0, 1)?.toUpperCase(Locale.ROOT)?.let {
                    FastScrollItemIndicator.Text(it)
                }
            }
        )
        fastscrollerThumbView.setupWithFastScroller(fastScrollerView)


        return root
    }


}