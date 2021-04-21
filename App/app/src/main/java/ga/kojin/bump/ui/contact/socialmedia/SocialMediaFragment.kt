package ga.kojin.bump.ui.contact.socialmedia

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.bump.R
import ga.kojin.bump.data.SocialMediaRepository
import ga.kojin.bump.models.SocialMediaType
import ga.kojin.bump.models.persisted.Contact
import ga.kojin.bump.models.persisted.SocialMedia

class SocialMediaFragment(val contact: Contact) : Fragment() {

    lateinit var root : View
    lateinit var socialMediaList: ArrayList<SocialMedia>

    private lateinit var socialMediaRV : RecyclerView
    private lateinit var addNewControl : LinearLayout

    private lateinit var chooseTypeImage : ImageView

    private lateinit var socialMediaAdapter : SocialMediaListAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        root = inflater.inflate(R.layout.fragment_social_media, container, false)

        socialMediaAdapter = SocialMediaListAdapter()

        socialMediaList = SocialMediaRepository(requireContext()).getSocialMediaByContactID(contact.id)
        socialMediaAdapter.socialMediaList = socialMediaList

        socialMediaRV = root.findViewById(R.id.socialMedia)

        socialMediaRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = socialMediaAdapter
        }

        addNewControl = root.findViewById(R.id.addNewControl)
        addNewControl.visibility = View.GONE

        val addNewButton : Button = root.findViewById(R.id.btnAdd)
        val txtHandle : TextView = root.findViewById(R.id.handle)
        val chooseType : ImageView = root.findViewById(R.id.imgChooseType)
        val clearChanges : ImageView = root.findViewById(R.id.imgClearChanges)
        val acceptChanges : ImageView = root.findViewById(R.id.imgAcceptChanges)

        addNewButton.setOnClickListener {
            addNewControl.visibility = View.VISIBLE
            addNewButton.visibility = View.INVISIBLE
        }

        chooseType.setOnClickListener {
            val dialog = Dialog(this.requireContext())
            dialog.setContentView(R.layout.dialog_social_media_type_picker)

            val picker : NumberPicker = dialog.findViewById(R.id.picker)
            picker.displayedValues = SocialMediaType.getValuesAsArray()
            picker.maxValue = SocialMediaType.getValuesAsArray().size - 1
            picker.minValue = 0

            val btnCancel : Button = dialog.findViewById(R.id.btnCancel)
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            val dialogButton : Button = dialog.findViewById(R.id.btnAccept)
            dialogButton.setOnClickListener {
                dialog.dismiss()
                chooseType.setImageResource(SocialMediaType.getImageResource(picker.value))
                chooseType.tag = SocialMediaType.getTypeFromInt(picker.value)
            }

            dialog.show()
        }

        acceptChanges.setOnClickListener {
            addNewControl.visibility = View.GONE
            val socialMedia = SocialMedia(-1, contact.id ?: -1,
                (chooseType.tag ?: SocialMediaType.Facebook) as SocialMediaType,
                txtHandle.text.toString()
                )
            SocialMediaRepository(requireContext()).addSocialMedia(socialMedia)
            socialMediaAdapter.socialMediaList.add(socialMedia)
            socialMediaAdapter.notifyItemInserted(
                socialMediaAdapter.socialMediaList.size - 1)
            addNewButton.visibility = View.VISIBLE
        }

        clearChanges.setOnClickListener {
            addNewControl.visibility = View.GONE
            txtHandle.text = ""
            addNewButton.visibility = View.VISIBLE
        }

        return root
    }

    fun setEdit(editMode : Boolean) {
        socialMediaAdapter.editMode = editMode
        socialMediaAdapter.notifyDataSetChanged()
    }

    fun saveDetails() {
        for(i in 0 until socialMediaList.size) {
            val socialVH = socialMediaRV.findViewHolderForAdapterPosition(0) as SocialMediaListAdapter.ViewHolder
            val socialMedia = socialMediaList[i]
            socialMedia.handle = socialVH.txtHandle.text.toString()
            SocialMediaRepository(requireContext()).updateSocialMedia(socialMedia)
        }
        socialMediaAdapter.notifyDataSetChanged()
    }
}