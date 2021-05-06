package ga.kojin.queshare.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import ga.kojin.queshare.R

class DialogConfirm(
    context: Context,
    val title: String,
    private val message: String,
    private val positiveText: String,
    private val negativeText: String,
    private val onPositive: () -> Unit,
    private val onNegative: () -> Unit
) :
    Dialog(context, R.style.CustomAlertDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)

        val txtTitle: TextView = findViewById(R.id.txtTitle)
        val txtMessage: TextView = findViewById(R.id.txtMessage)
        val btnPositive: Button = findViewById(R.id.btnPositive)
        val btnNegative: Button = findViewById(R.id.btnNegative)

        txtTitle.text = title
        txtMessage.text = message
        btnPositive.text = positiveText
        btnNegative.text = negativeText

        btnPositive.text = positiveText
        btnPositive.setOnClickListener {
            onPositive()
            dismiss()
        }
        btnNegative.text = negativeText
        btnNegative.setOnClickListener {
            onNegative()
            dismiss()
        }


    }
}