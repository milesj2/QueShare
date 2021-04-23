package ga.kojin.bump.ui.contactview

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ga.kojin.bump.R
import ga.kojin.bump.models.persisted.Contact
import ga.kojin.bump.ui.contactview.basicdetails.BasicDetailsFragment
import ga.kojin.bump.ui.contactview.socialmedia.SocialMediaFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class ContactViewAdapter(var contact: Contact, val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    var basicDetailsFragment: BasicDetailsFragment = BasicDetailsFragment(contact)
    var socialMediaFragment: SocialMediaFragment = SocialMediaFragment(contact)

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                // basicDetailsFragment
                BasicDetailsFragment(contact)
            }
            1 -> {
                // socialMediaFragment
                SocialMediaFragment(contact)
            }
            else -> getItem(position)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    fun saveDetails(starred: Boolean) {
        basicDetailsFragment.saveDetails(starred)
        socialMediaFragment.saveDetails()
    }

    fun setEdit(editMode: Boolean){
        socialMediaFragment.setEdit(editMode)
        basicDetailsFragment.toggleEditMode(editMode)
    }
}