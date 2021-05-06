package ga.kojin.queshare.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ga.kojin.queshare.R

class DialogAlert(
    context: Context,
    val title: String,
    private val message: String,
    private val buttonText: String
) :
    Dialog(context, R.style.CustomAlertDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_alert)

        val txtTitle: TextView = findViewById(R.id.txtTitle)
        val txtMessage: TextView = findViewById(R.id.txtMessage)
        val btnDone: Button = findViewById(R.id.btnPositive)

        txtTitle.text = title
        txtMessage.text = message
        btnDone.text = buttonText
        btnDone.setOnClickListener {
            dismiss()
        }

    }
}