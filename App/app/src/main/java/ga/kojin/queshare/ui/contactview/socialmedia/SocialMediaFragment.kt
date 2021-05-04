package ga.kojin.queshare.ui.contactview.socialmedia

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ga.kojin.queshare.R
import ga.kojin.queshare.data.SocialMediaRepository
import ga.kojin.queshare.models.SocialMediaType
import ga.kojin.queshare.models.persisted.Contact
import ga.kojin.queshare.models.persisted.SocialMedia

class SocialMediaFragment() : Fragment() {

    private val TAG: String = "SocialMediaFragment"

    lateinit var root: View
    lateinit var socialMediaList: ArrayList<SocialMedia>

    private lateinit var socialMediaRV: RecyclerView
    private lateinit var addNewControl: LinearLayout
    private lateinit var chooseTypeImage: ImageView
    private lateinit var socialMediaAdapter: SocialMediaListAdapter
    private lateinit var addNewButton: Button
    var contact: Contact? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.w(TAG, "CREATED")

        root = inflater.inflate(R.layout.fragment_social_media, container, false)
        socialMediaAdapter = SocialMediaListAdapter(requireContext())

        socialMediaRV = root.findViewById(R.id.socialMedia)

        socialMediaRV.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = socialMediaAdapter
        }

        addNewControl = root.findViewById(R.id.addNewControl)
        addNewControl.visibility = View.GONE

        addNewButton = root.findViewById(R.id.btnAdd)
        val txtHandle: TextView = root.findViewById(R.id.handle)
        val chooseType: ImageView = root.findViewById(R.id.imgChooseType)
        val clearChanges: ImageView = root.findViewById(R.id.imgClearChanges)
        val acceptChanges: ImageView = root.findViewById(R.id.imgAcceptChanges)

        addNewButton.setOnClickListener {
            addNewButton.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.move_down_fade_out
                )
            )
            addNewControl.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.dialog_fade_in
                )
            )
            addNewButton.postDelayed({
                addNewButton.animation = null
                addNewButton.visibility = View.GONE
                addNewControl.visibility = View.VISIBLE
            }, 150)
        }

        chooseType.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_social_media_type_picker)

            val picker: NumberPicker = dialog.findViewById(R.id.picker)
            picker.displayedValues = SocialMediaType.getValuesAsArray()
            picker.maxValue = SocialMediaType.getValuesAsArray().size - 1
            picker.minValue = 0

            val btnCancel: Button = dialog.findViewById(R.id.btnCancel)
            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            val dialogButton: Button = dialog.findViewById(R.id.btnAccept)
            dialogButton.setOnClickListener {
                dialog.dismiss()
                chooseType.setImageResource(SocialMediaType.getImageResource(picker.value))
                chooseType.tag = SocialMediaType.getTypeFromInt(picker.value)
            }

            dialog.show()
        }

        acceptChanges.setOnClickListener {

            val socialMedia = SocialMedia(
                -1,
                contact!!.id,
                (chooseType.tag ?: SocialMediaType.Facebook) as SocialMediaType,
                txtHandle.text.toString()
            )
            SocialMediaRepository(requireContext()).addSocialMedia(socialMedia)
            if (txtHandle.text.toString() != "") {
                socialMediaAdapter.socialMediaList.add(socialMedia)
                socialMediaAdapter.notifyItemInserted(
                    socialMediaAdapter.socialMediaList.size - 1
                )
            }
            animateEditBoxOut()
            txtHandle.text = ""
        }

        clearChanges.setOnClickListener {
            animateEditBoxOut()
            txtHandle.text = ""
        }

        return root
    }

    fun setEdit(editMode: Boolean) {
        socialMediaAdapter.editMode = editMode
        socialMediaAdapter.notifyDataSetChanged()
    }

    fun saveDetails(): Boolean {
        return try {
            for (i in 0 until socialMediaList.size) {
                val socialVH =
                    socialMediaRV.findViewHolderForAdapterPosition(0) as SocialMediaListAdapter.ViewHolder
                val socialMedia = socialMediaList[i]
                socialMedia.handle = socialVH.txtHandle.text.toString()
                SocialMediaRepository(requireContext()).updateSocialMedia(socialMedia)
            }
            socialMediaAdapter.notifyDataSetChanged()
            true
        } catch (e: UninitializedPropertyAccessException) {
            false
        }
    }

    private fun animateEditBoxOut() {
        addNewControl.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.move_down_fade_out
            )
        )
        addNewButton.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.dialog_fade_in
            )
        )
        addNewButton.postDelayed({
            addNewControl.animation = null
            addNewControl.visibility = View.GONE
            addNewButton.visibility = View.VISIBLE
        }, 150)
    }

    fun refreshSocialMediaList() {
        if (contact != null) {
            socialMediaList =
                SocialMediaRepository(requireContext()).getSocialMediaByContactID(contact!!.id)
        }
        socialMediaAdapter.socialMediaList = socialMediaList
        socialMediaAdapter.notifyDataSetChanged()

    }

    override fun onResume() {
        super.onResume()
        refreshSocialMediaList()
    }
}