package ga.kojin.bump.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ga.kojin.bump.R
import ga.kojin.bump.ui.contact.ProfileViewFragment
import ga.kojin.bump.ui.contacts.ContactsFragment
import ga.kojin.bump.ui.favourites.FavouritesFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    var contactsFragment: ContactsFragment = ContactsFragment()
    var favouritesFragment: FavouritesFragment = FavouritesFragment()
    var profileViewFragment: ProfileViewFragment = ProfileViewFragment()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                favouritesFragment
            }
            1 -> {
                contactsFragment
            }
            2 -> {
                profileViewFragment
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
}