package ga.kojin.queshare.helpers

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object QueShareDialogHelper {

    fun showChoiceDialog(
        context: Context,
        title: String,
        message: String,
        positiveText: String,
        negativeText: String,
        cancellable: Boolean,
        onCancel: (alert: DialogInterface) -> Unit,
        onPositive: (alert: DialogInterface) -> Unit
    ) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage(message)
            .setCancelable(cancellable)
            .setPositiveButton(positiveText) { dialog, _ ->
                onPositive(dialog)
            }
            .setNegativeButton(negativeText) { dialog, _ ->
                onCancel(dialog)
            }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }

    fun showAlertDialog(context: Context, title: String, message: String, buttonText: String) {
        val dialogBuilder = AlertDialog.Builder(context)

        dialogBuilder.setMessage(message)
            .setPositiveButton(buttonText) { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
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