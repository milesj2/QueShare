package ga.kojin.bumpup.ui.contacts

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bumpup.R
import ga.kojin.bumpup.interfaces.IViewHolderClickListener

class ContactsViewHolder(itemView : View,
                         val r_tap: IViewHolderClickListener)
    : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener,
        View.OnClickListener{

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