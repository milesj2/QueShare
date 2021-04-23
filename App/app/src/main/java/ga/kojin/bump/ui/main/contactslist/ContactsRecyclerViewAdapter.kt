package ga.kojin.bump.ui.main.contactslist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.models.persisted.Contact
import ga.kojin.bump.ui.contactview.ContactViewActivity

class ContactsRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>(),
    IClickListener {

    private val TAG: String = "ContactRecyclerViewAdapter"

    var contactsList: ArrayList<Contact> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_contact,
            parent,
            false
        )
        return ViewHolder(view, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact: Contact = contactsList[position]

        holder.contactViewName.text = contact.name

        val id = contactsList[position].id
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    class ViewHolder(itemView: View, private val clickListener: IClickListener) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener {

        val contactViewName = itemView.findViewById<TextView>(R.id.txtName)
        val contactsLinearLayout = itemView.findViewById<LinearLayout>(R.id.contactsLinear)

        init {
            contactsLinearLayout!!.setOnClickListener(this)
            contactsLinearLayout.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            clickListener.onTap(adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            clickListener.onLongTap(adapterPosition)
            return true
        }
    }

    override fun onLongTap(index: Int) {
        Toast.makeText(context, "Clicked!", Toast.LENGTH_LONG).show()
    }

    override fun onTap(index: Int) {
        val intent = Intent("ga.kojin.bump.ui.contact.ContactActivity").apply {
            putExtra("Contact", contactsList[index].id)
        }
        intent.setClass(context, ContactViewActivity::class.java)
        context.startActivity(intent)
    }

}
