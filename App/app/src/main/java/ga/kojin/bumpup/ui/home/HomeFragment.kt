package ga.kojin.bumpup.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bumpup.R
import ga.kojin.bumpup.interfaces.IContactsInterface
import ga.kojin.bumpup.ui.contacts.ContactsAdapter
import ga.kojin.bumpup.ui.contacts.GetContacts

class HomeFragment : Fragment(), IContactsInterface {

    private lateinit var homeViewModel: HomeViewModel

    override fun contactsInterface(size: Int) {
         /*
        if (actionMode == null) actionMode = startActionMode(ActionModeCallback())
        if (size > 0) actionMode?.setTitle("$size")
        else actionMode?.finish()

          */
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val recyclerView: RecyclerView = root.findViewById(R.id.contact_list)

        val contacts = this.context?.let { GetContacts().getContactList(it) }
        val contactsAdapter = contacts?.let { ContactsAdapter(this, it) }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        return root
    }
}