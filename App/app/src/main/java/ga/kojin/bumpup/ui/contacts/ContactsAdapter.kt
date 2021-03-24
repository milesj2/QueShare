package ga.kojin.bumpup.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bumpup.R
import ga.kojin.bumpup.interfaces.IContactsInterface
import ga.kojin.bumpup.models.ContactsRow

class ContactsAdapter(val contactsInterface: IContactsInterface,
                      val contactsList: ArrayList<ContactsRow>)
    : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_row,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: ContactsRow = contactsList[position]

        holder.contactViewName.text = contact.name

        val id = contactsList[position].id

    }

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        val contactViewName = itemView.findViewById<TextView>(R.id.contactViewName)
        val contactsLinearLayout = itemView.findViewById<LinearLayout>(R.id.contactsLinear)
        val contactsCardView = itemView.findViewById<CardView>(R.id.contactsCard)
    }
}