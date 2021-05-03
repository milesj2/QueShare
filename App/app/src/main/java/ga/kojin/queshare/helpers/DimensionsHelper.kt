package ga.kojin.queshare.helpers

import android.content.res.Resources
import android.util.TypedValue

object DimensionsHelper {

    fun dpToPx(dp: Float): Float =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().displayMetrics
        )

    fun getWindowWidth(): Int = Resources.getSystem().displayMetrics.widthPixels

    fun getWindowHeight(): Int = Resources.getSystem().displayMetrics.heightPixels

}