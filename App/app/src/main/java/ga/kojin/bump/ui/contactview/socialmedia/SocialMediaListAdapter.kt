package ga.kojin.bump.ui.contactview.socialmedia

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.models.SocialMediaType
import ga.kojin.bump.models.persisted.SocialMedia


class SocialMediaListAdapter() :
    RecyclerView.Adapter<SocialMediaListAdapter.ViewHolder>() {

    var socialMediaList: ArrayList<SocialMedia> = ArrayList()
    var editMode: Boolean = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val socialMediaIco: ImageView = itemView.findViewById(R.id.imgSocialMediaIco)
        val txtHandle: TextView = itemView.findViewById(R.id.handle)

    }

    override fun getItemViewType(position: Int): Int {
        return if (editMode) {
            R.layout.row_social_media_edit
        } else {
            R.layout.row_social_media
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (editMode) {
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_social_media_edit,
                parent,
                false
            )
        } else {
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_social_media,
                parent,
                false
            )
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val socialMedia: SocialMedia = socialMediaList[position]

        holder.socialMediaIco.setImageResource(SocialMediaType.getImageResource(socialMedia.type))

        holder.txtHandle.text = socialMedia.handle

        holder.txtHandle.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    override fun getItemCount(): Int {
        return socialMediaList.size
    }

    fun saveDetails() {

    }
}