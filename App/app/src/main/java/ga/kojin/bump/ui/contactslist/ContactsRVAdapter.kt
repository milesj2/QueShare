package ga.kojin.bump.ui.contactslist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.models.SystemContact

class ContactsRVAdapter(val context : Context)
    : RecyclerView.Adapter<ContactsRVAdapter.ViewHolder>()
{

    var contactsList : ArrayList<SystemContact> = ArrayList<SystemContact>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_row,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: SystemContact = contactsList[position]

        holder.contactViewName.text = contact.name

        val id = contactsList[position].id
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    class ViewHolder(itemView : View)
        : RecyclerView.ViewHolder(itemView) {

        val contactViewName = itemView.findViewById<TextView>(R.id.txtName)
        val contactsLinearLayout = itemView.findViewById<LinearLayout>(R.id.contactsLinear)
    }

}