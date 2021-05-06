package ga.kojin.queshare.helpers

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.ImageView
import ga.kojin.queshare.R

object AnimationHelper {

    fun animateButtons(
        context: Context,
        btn1: ImageView,
        btn2: ImageView,
        btn3: ImageView,
        btn4: ImageView
    ) {
        btn1.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.btn_right_2)
        )
        btn2.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.btn_right_1)
        )

        btn3.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.btn_fade_in)
        )
        btn4.startAnimation(
            AnimationUtils.loadAnimation(context, R.anim.btn_fade_in)
        )
    }

}