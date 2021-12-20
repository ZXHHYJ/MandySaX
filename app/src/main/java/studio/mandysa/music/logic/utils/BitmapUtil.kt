package studio.mandysa.music.logic.utils

import android.graphics.*


/**
 * @author ZXHHYJ
 */
object BitmapUtil {
    fun handleImageEffect(bm: Bitmap, saturation: Float): Bitmap {
        val saturationMatrix = ColorMatrix()
        saturationMatrix.setSaturation(saturation)
        val imageMatrix = ColorMatrix()
        imageMatrix.postConcat(saturationMatrix)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(imageMatrix)
        val bitmap = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(bm, 0f, 0f, paint)
        bm.recycle()
        return bitmap
    }

}