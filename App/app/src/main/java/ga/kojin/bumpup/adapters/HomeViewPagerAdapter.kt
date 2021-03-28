package ga.kojin.bumpup.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ga.kojin.bumpup.ui.contacts.ContactsFragment

internal class HomeViewPagerAdapter(
        var context: Context,
        fm: FragmentManager,
        var totalTabs: Int
    ) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    ContactsFragment()
                }
                1 -> {
                    ContactsFragment()
                }
                2 -> {
                    ContactsFragment()
                }
                else -> getItem(position)
            }
        }

        override fun getCount(): Int {
            return totalTabs
        }
}