package ga.kojin.bumpup

import android.os.Bundle
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sembozdemir.permissionskt.askPermissions
import ga.kojin.bumpup.helpers.SystemContactsHelper
import ga.kojin.bumpup.models.SystemContact
import com.sembozdemir.permissionskt.*
import ga.kojin.bumpup.data.ContactsRepository

class MainActivity : AppCompatActivity() {


    data class ItemViewState(val text: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS) {
            onGranted {
                toast("Fresesjing.")

            }
            onDenied {
                toast("Denied.")
            }
            onShowRationale {
                toast("Show rationale.")
            }

            onNeverAskAgain {
                toast("Never Ask Again.")
            }
        }

        setContent{
            displayMain()
        }
    }

    @Preview
    @Composable
    fun displayMain(){
        var state by remember { mutableStateOf(0) }
        val titles = listOf("Contacts", "Favourites", "Groups")

        val contacts = ContactsRepository(this.applicationContext).getUsers()

        Column {
            TabRow(selectedTabIndex = state) {

                titles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = state == index,
                        onClick = { state = index },
                    )
                }
            }
            if (state == 0){
                if (contacts != null && contacts.count() > 0){
                    ContactsList(contactsList = contacts)
                } else {
                    Text(modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .wrapContentHeight(Alignment.CenterVertically),
                        text = "Please add contacts or, ")
                    Button(onClick = { importContacts() }) {
                        Text("Import contacts")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ContactsList(contactsList: ArrayList<SystemContact>) {

        val sections = listOf("A".."Z")

        LazyColumn {
            sections.forEach { section ->
                stickyHeader {
                    Text(
                        "TODO Section $section",
                        Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray)
                            .padding(8.dp)
                    )
                }
            }

            items(contactsList) { contact ->
                newContactRow(contact = contact)
            }
        }
    }

    @Composable
    fun newContactRow(contact: SystemContact) {
        Row {
            val imageModifier =
                Modifier
                    .requiredSize(46.dp)
                    .shadow(elevation = 10.dp, shape = CircleShape)
                    .background(color = Color.DarkGray, shape = CircleShape)
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_person_24),
                contentDescription = "Placeholder Contact Image"
            )
            Text(text = contact.name)
            Text(text = contact.number)
        }
    }

    private fun toast(messsage: String) {
        Toast.makeText(this, messsage, Toast.LENGTH_LONG).show()
    }

    private fun importContacts() {
        val contactsRepo = ContactsRepository(applicationContext)
        val contacts = SystemContactsHelper().getContactList(applicationContext)
        contacts?.forEach{contact ->
            contactsRepo.addUser(contact)
        }

        toast("Finished importing...")

        setContent{
            displayMain()
        }
    }
}