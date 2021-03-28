package ga.kojin.bumpup.ui.contacts

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reddit.indicatorfastscroll.FastScrollItemIndicator
import com.reddit.indicatorfastscroll.FastScrollerThumbView
import com.reddit.indicatorfastscroll.FastScrollerView
import ga.kojin.bumpup.R
import ga.kojin.bumpup.adapters.ContactsAdapter
import ga.kojin.bumpup.data.ContactsRepository
import ga.kojin.bumpup.interfaces.IContactsInterface
import java.util.*

class ContactsFragment : Fragment(), IContactsInterface {

    private lateinit var homeViewModel: ContactsViewModel

    val contactsRepo = ContactsRepository(requireContext())

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
    ): View?
    {

        homeViewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contacts, container, false)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        val recyclerView: RecyclerView = root.findViewById(R.id.contact_list)

        val contacts = contactsRepo.getUsers()
        if (contacts.count() == 0){
            val dialogBuilder = AlertDialog.Builder(requireContext())

            dialogBuilder.setMessage("No contacts are stored in app, would you like to import?")
                .setCancelable(false)
                .setPositiveButton("Proceed") { dialog, _ ->
                    dialog.cancel()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            val alert = dialogBuilder.create()
            alert.setTitle("AlertDialogExample")

            alert.show()
        }

        val contactsAdapter =  ContactsAdapter(this, requireContext(), contacts)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = contactsAdapter
        }

        val fastScrollerView: FastScrollerView = root.findViewById(R.id.fastscroller)
        val fastscrollerThumbView: FastScrollerThumbView = root.findViewById(R.id.fastscroller_thumb)

        fastScrollerView.setupWithRecyclerView(
            recyclerView,
            { position ->
                val item = contacts?.get(position) // Get your model object
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