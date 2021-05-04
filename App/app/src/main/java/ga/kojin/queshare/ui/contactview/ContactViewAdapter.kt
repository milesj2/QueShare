package ga.kojin.queshare.ui.contactview

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ga.kojin.queshare.R
import ga.kojin.queshare.models.persisted.Contact
import ga.kojin.queshare.ui.contactview.basicdetails.BasicDetailsFragment
import ga.kojin.queshare.ui.contactview.socialmedia.SocialMediaFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class ContactViewAdapter(val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var basicDetailsFragment: BasicDetailsFragment = BasicDetailsFragment()
    private var socialMediaFragment: SocialMediaFragment = SocialMediaFragment()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                basicDetailsFragment
            }
            1 -> {
                socialMediaFragment
            }
            else -> getItem(position)
        }
    }

    override fun getPageTitle(position: Int): CharSequence =
        context.resources.getString(TAB_TITLES[position])

    override fun getCount(): Int = TAB_TITLES.size

    fun setContact(contact: Contact) {
        socialMediaFragment.contact = contact
        basicDetailsFragment.contact = contact
    }

    fun saveDetails(starred: Boolean): Boolean {
        if (!basicDetailsFragment.saveDetails(starred))
            return false
        return socialMediaFragment.saveDetails()
    }

    fun setEdit(editMode: Boolean) {
        socialMediaFragment.setEdit(editMode)
        basicDetailsFragment.toggleEditMode(editMode)
    }
}