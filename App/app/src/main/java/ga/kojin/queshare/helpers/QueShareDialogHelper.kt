package ga.kojin.queshare.helpers

import android.content.Context

import androidx.appcompat.app.AlertDialog

import ga.kojin.queshare.ui.dialogs.DialogAlert
import ga.kojin.queshare.ui.dialogs.DialogConfirm


object QueShareDialogHelper {

    fun showChoiceDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        cancellable: Boolean,
        onPositive: () -> Unit,
        onNegative: () -> Unit
    ) {
        val dialogBuilder = DialogConfirm(
            context,
            title,
            message,
            positiveText,
            negativeText,
            onPositive,
            onNegative
        )
        dialogBuilder.show()
    }

    fun showAlertDialog(
        context: Context,
        title: String,
        message: String,
        buttonText: String
    ) {

        val dialogBuilder = DialogAlert(context, title, message, buttonText)
        dialogBuilder.show()

    }

    fun showProfileNotInitialisedDialog(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage("Please setup your profile to use this feature.")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Sorry!")
        alert.show()
    }

}