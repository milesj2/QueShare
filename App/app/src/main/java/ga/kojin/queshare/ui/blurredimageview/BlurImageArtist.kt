package ga.kojin.queshare.ui.blurredimageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.RoundedCornersTransformation
import ga.kojin.queshare.R

class BlurImageArtist : RelativeLayout {
    /**
     * Core Items
     */
    private var mContext: Context? = null
    private var attrs: AttributeSet? = null
    private var styleAttr = 0
    private var view: View? = null

    /**
     * Core Components
     */
    var image: ImageView? = null
    var alphaLayer: ImageView? = null

    /**
     * Attributes
     */
    var imageFile: Drawable? = null
    var imagePlaceHolder: Drawable? = null
    var imageError: Drawable? = null
    var blur: Drawable? = null

    constructor(context: Context?) : super(context) {
        mContext = context
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        this.attrs = attrs
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mContext = context
        this.attrs = attrs
        styleAttr = defStyleAttr
        initView()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    private fun initView() {
        view = this
        View.inflate(context, R.layout.layout_blur_image, this)
        val arr = context!!.obtainStyledAttributes(attrs, R.styleable.BlurImageArtist, styleAttr, 0)
        imageFile = arr.getDrawable(R.styleable.BlurImageArtist_imageSrc)
        imageError = arr.getDrawable(R.styleable.BlurImageArtist_imageError)
        imagePlaceHolder = arr.getDrawable(R.styleable.BlurImageArtist_imagePlaceholder)
        blur = arr.getDrawable(R.styleable.BlurImageArtist_blur)
        image = findViewById(R.id.wall)
        alphaLayer = findViewById(R.id.backgroud)
        if (imageFile != null) {
            setDrawableImage(imageFile)
        }

        arr.recycle()
    }

    /*public void setScaleType(Coil){
        image.setScaleType(scaleType);
    }*/

    fun setDrawableImage(imageFile: Drawable?) {
        image!!.setImageDrawable(imageFile)
    }

    fun setDrawableImage(
        imageFile: Int,
        imageError: Int,
        imagePlaceHolder: Int,
        radius: Float,
        sampling: Float
    ) {

        image?.load(imageFile) {
            data(imageFile)
            placeholder(imagePlaceHolder)
            error(imageError)
            transformations(RoundedCornersTransformation())
        }
        alphaLayer?.load(imageFile) {
            data(imageFile)
            placeholder(imagePlaceHolder)
            error(imageError)
            transformations(BlurTransformation(context, radius, sampling))
        }

    }

    fun setDrawableImage(
        imageFile: Bitmap,
        imageError: Int,
        imagePlaceHolder: Int,
        radius: Float,
        sampling: Float
    ) {

        image?.load(imageFile) {
            data(imageFile)
            placeholder(imagePlaceHolder)
            error(imageError)
            transformations(RoundedCornersTransformation())
        }
        alphaLayer?.load(imageFile) {
            data(imageFile)
            placeholder(imagePlaceHolder)
            error(imageError)
            transformations(BlurTransformation(context, radius, sampling))
        }

    }

    fun setUrlImage(
        url: String,
        imageError: Int,
        imagePlaceHolder: Int,
        radius: Float,
        sampling: Float
    ) {
        image?.load(url) {
            data(url)
            placeholder(imagePlaceHolder)
            error(imageError)
            transformations(RoundedCornersTransformation())
        }
        alphaLayer?.load(url) {
            data(url)
            placeholder(imagePlaceHolder)
            error(imageError)
            transformations(BlurTransformation(context, radius, sampling))
        }
    }

    fun setResImage(resId: Int, radius: Float, sampling: Float) {
        image?.load(resId) {
            data(resId)
            transformations(RoundedCornersTransformation())
        }
        alphaLayer?.load(resId) {
            data(resId)
            transformations(BlurTransformation(context, radius, sampling))
        }
    }
}