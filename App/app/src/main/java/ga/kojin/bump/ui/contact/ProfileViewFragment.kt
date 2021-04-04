package ga.kojin.bump.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ga.kojin.bump.R
import ga.kojin.bump.data.ContactsRepository
import ga.kojin.bump.models.SystemContact

class ProfileViewFragment : Fragment() {

    private lateinit var contact : SystemContact
    private lateinit var contactsRepo : ContactsRepository
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val root = inflater.inflate(R.layout.activity_contact_details, container, false)

        contactsRepo = ContactsRepository(root.context)
        contact = contactsRepo.getUserProfile()

        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        val starred: ImageView = root.findViewById(R.id.imgStarred)
        val title: TextView = root.findViewById(R.id.txtName)
        val navback: ImageView = root.findViewById(R.id.imgNavBack)
        val avatar: ImageView = root.findViewById(R.id.imageView)

        toolbar.visibility = View.GONE
        starred.visibility = View.GONE
        title.visibility = View.GONE
        navback.visibility = View.GONE

        avatar.setImageResource(R.mipmap.ic_default_avatar)

        val contactAdapter = ContactAdapter(contact, root.context, this.requireFragmentManager())

        viewPager = root.findViewById(R.id.detailsViewPager)
        tabLayout = root.findViewById(R.id.detailsTabBar)

        viewPager.adapter = contactAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return root
    }

}