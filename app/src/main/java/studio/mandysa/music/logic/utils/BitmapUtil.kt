package studio.mandysa.music.logic.utils

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur


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

    fun doBlur(context: Context, image: Bitmap, radius: Float): Bitmap {
        val rs = RenderScript.create(context)
        val outputBitmap = Bitmap.createScaledBitmap(image, image.width, image.height, false)
        val `in` = Allocation.createFromBitmap(rs, image)
        val out = Allocation.createFromBitmap(rs, outputBitmap)
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        intrinsicBlur.setRadius(radius)
        intrinsicBlur.setInput(`in`)
        intrinsicBlur.forEach(out)
        out.copyTo(outputBitmap)
        image.recycle()
        rs.destroy()
        return outputBitmap
    }

}