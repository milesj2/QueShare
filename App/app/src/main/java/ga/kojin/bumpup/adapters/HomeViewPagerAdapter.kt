package ga.kojin.bumpup.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ga.kojin.bumpup.ui.contacts.ContactsFragment
import ga.kojin.bumpup.ui.contacts.FavouritesActivity
import ga.kojin.bumpup.ui.contacts.GroupsActivity

internal class HomeViewPagerAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    val contactsFragment: ContactsFragment = ContactsFragment()
    val favouritesActivity: FavouritesActivity = FavouritesActivity()
    val groupsActivity: GroupsActivity = GroupsActivity()

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    contactsFragment
                }
                1 -> {
                    favouritesActivity
                }
                2 -> {
                    groupsActivity
                }
                else -> getItem(position)
            }
        }

        override fun getCount(): Int {
            return totalTabs
        }
}