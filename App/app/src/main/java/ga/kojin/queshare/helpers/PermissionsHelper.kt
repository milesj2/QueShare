package ga.kojin.queshare.helpers

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

object PermissionsHelper {

    fun checkPermission(context: Activity, permission: String): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_DENIED

    fun requestPermissions(context: Fragment, permissions: Array<String>, dReturn: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(permissions, dReturn)
        } else {
            ActivityCompat.requestPermissions(context.requireActivity(), permissions, dReturn)
        }
    }

    fun requestPermissions(context: Activity, permissions: Array<String>, dReturn: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(permissions, dReturn)
        } else {
            ActivityCompat.requestPermissions(context, permissions, dReturn)
        }
    }

    fun requestPermission(context: Activity, permission: String, dReturn: Int) {

        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(arrayOf(permission), dReturn)
            } else {
                ActivityCompat.requestPermissions(context, arrayOf(permission), dReturn)
            }

        }
    }

    fun requestPermission(context: Fragment, permission: String, dReturn: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.requestPermissions(arrayOf(permission), dReturn)
        } else {
            ActivityCompat.requestPermissions(
                context.requireActivity(),
                arrayOf(permission),
                dReturn
            )
        }
    }
}