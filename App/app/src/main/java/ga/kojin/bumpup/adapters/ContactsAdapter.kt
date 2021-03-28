package ga.kojin.bumpup.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bumpup.R
import ga.kojin.bumpup.interfaces.IContactsInterface
import ga.kojin.bumpup.interfaces.IViewHolderClickListener
import ga.kojin.bumpup.models.ContactsRow
import ga.kojin.bumpup.ui.contact.ContactActivity

class ContactsAdapter(val contactsInterface: IContactsInterface,
                      val context : Context,
                      val contactsList: ArrayList<ContactsRow>)
    : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>(), IViewHolderClickListener
{


    override fun onLongTap(index: Int) {

    }

    override fun onTap(index: Int) {
        val intent = Intent("ga.kojin.bumpup.ContactActivity")
        intent.setClass(context, ContactActivity::class.java)
        context.startActivity(intent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_row,
            parent,
            false
        )
        return ContactsViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact: ContactsRow = contactsList[position]

        holder.contactViewName.text = contact.name

        val id = contactsList[position].id

    }

    class ContactsViewHolder(itemView : View,
                             val r_tap: IViewHolderClickListener)
        : RecyclerView.ViewHolder(itemView),
            View.OnLongClickListener,
            View.OnClickListener{

        val contactViewName = itemView.findViewById<TextView>(R.id.contactViewName)
        val contactsLinearLayout = itemView.findViewById<LinearLayout>(R.id.contactsLinear)

        init {
            contactsLinearLayout.setOnClickListener(this)
            contactsLinearLayout.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            r_tap.onTap(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            r_tap.onLongTap(adapterPosition)
            return true
        }

    }
}