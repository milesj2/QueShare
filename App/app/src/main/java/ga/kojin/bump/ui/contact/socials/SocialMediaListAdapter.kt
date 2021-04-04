package ga.kojin.bump.ui.contact.socials

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.models.SocialMediaType
import ga.kojin.bump.models.SystemContact
import ga.kojin.bump.models.persisted.SocialMedia
import ga.kojin.bump.ui.contactslist.ContactsRecyclerViewAdapter
import ga.kojin.bump.ui.contactslist.IClickListener
import org.w3c.dom.Text

class SocialMediaListAdapter() :
    RecyclerView.Adapter<SocialMediaListAdapter.ViewHolder>() {

    var socialMediaList : ArrayList<SocialMedia> = ArrayList()

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val socialMediaIco : ImageView = itemView.findViewById(R.id.imgSocialMediaIco)
        val txtHandle : TextView = itemView.findViewById(R.id.handle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.social_media_row,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socialMedia: SocialMedia = socialMediaList[position]

        holder.socialMediaIco.setImageResource(SocialMediaType.getImageResource(socialMedia.type))

        holder.txtHandle.text = socialMedia.handle
    }

    override fun getItemCount(): Int {
        return socialMediaList.size
    }


}