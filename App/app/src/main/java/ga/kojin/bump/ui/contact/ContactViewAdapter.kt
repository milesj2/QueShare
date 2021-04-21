package ga.kojin.bump.ui.contact

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ga.kojin.bump.R
import ga.kojin.bump.models.persisted.Contact
import ga.kojin.bump.ui.contact.basicdetails.BasicDetailsFragment
import ga.kojin.bump.ui.contact.socialmedia.SocialMediaFragment


private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

class ContactViewAdapter(var contact: Contact, val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    var basicDetailsFragment: BasicDetailsFragment = BasicDetailsFragment(contact)
    var socialMediaFragment: SocialMediaFragment = SocialMediaFragment(contact)

    fun setEdit(editMode: Boolean){
        socialMediaFragment.setEdit(editMode)
        basicDetailsFragment.toggleEditMode(editMode)
    }

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
}