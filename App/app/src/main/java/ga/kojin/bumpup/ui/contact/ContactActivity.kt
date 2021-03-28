package ga.kojin.bumpup.ui.contact

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import ga.kojin.bumpup.R
import ga.kojin.bumpup.models.ContactRow
import ga.kojin.bumpup.ui.contacts.GetContacts

class ContactActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_edit)


        setContent{
            NewsStory()
        }
    }

    @Composable
    fun DefaultPreview() {
        NewsStory()
    }

    @Composable
    fun NewsStory() {
        var contactIdx = 0
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.test_drawable),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))

            var state by remember { mutableStateOf(0) }
            val titles = listOf("Basic Details", "Social Media")

            Column {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = state == index,
                            onClick = { state = index }
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                if (state == 0){
                    BasicDetails(contactIdx)
                } else {
                    SocialMedia()
                }
            }
        }
    }
    @Composable
    fun BasicDetails(contactIdx: Int) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            var contact: ContactRow?

            if (contactIdx != -1){
                contact = GetContacts().getContactList(applicationContext)!![contactIdx]
                DisplayRow(icon = R.drawable.ic_baseline_person_24, value = contact.id)
                DisplayRow(icon = R.drawable.ic_baseline_person_24, value = contact.name)
                DisplayRow(icon = R.drawable.ic_baseline_local_phone_24, value = contact.id)
            }
            if (contactIdx == -1){
                // Display blank contact
            }

        }
    }

    @Composable
    fun SocialMedia() {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Social Media")
        }
    }

    @Composable
    fun DisplayRow(icon: Int, value: String){
        Row(horizontalArrangement = Arrangement.SpaceEvenly){
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(4.dp))
            Text(text = value)
        }
    }
}





