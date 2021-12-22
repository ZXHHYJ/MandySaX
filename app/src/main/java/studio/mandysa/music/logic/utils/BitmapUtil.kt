package studio.mandysa.music.logic.utils

import android.content.Context
import android.graphics.*
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import studio.mandysa.music.R
import kotlin.math.roundToInt


/**
 * @author ZXHHYJ
 */
object BitmapUtil {

    fun handleImageBlur(context: Context, image: Bitmap): Bitmap {
        var blurBitmap = if (isWallpaperBright(image)) drawCover(
            image,
            context.getColor(R.color.translucent_black)
        ) else image
        val w = 150
        blurBitmap =
            scaleBitmap(blurBitmap, w, blurBitmap.height * w / blurBitmap.width)
        blurBitmap = doBlur(context, blurBitmap, 25f)
        blurBitmap = scaleBitmap(blurBitmap, blurBitmap.width, blurBitmap.height)
        blurBitmap = doBlur(context, blurBitmap, 24f)
        blurBitmap = handleImageEffect(blurBitmap)
        return blurBitmap
    }

    private fun handleImageEffect(bm: Bitmap): Bitmap {
        val imageMatrix = ColorMatrix()
        imageMatrix.setSaturation(1.8f)
        imageMatrix.setScale(0.98F, 1F, 0.98F, 1F)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(imageMatrix)
        val canvas = Canvas(bm)
        canvas.drawBitmap(bm, 0f, 0f, paint)
        return bm
    }

    private fun doBlur(context: Context, image: Bitmap, radius: Float): Bitmap {
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

    private const val SCALE_RATIO_FACTOR = 100.0f

    private fun isWallpaperBright(wallpaper: Bitmap): Boolean {
        val scaledBitmap = scaleBitmapDown(wallpaper)
        val result = isBitmapBright(scaledBitmap, scaledBitmap.width, scaledBitmap.height)
        if (scaledBitmap != wallpaper) {
            scaledBitmap.recycle()
        }
        return result
    }

    private fun isBitmapBright(bitmap: Bitmap, width: Int, height: Int): Boolean {
        var hitCount = 0f
        val w = width.coerceAtMost(bitmap.width)
        val h = height.coerceAtMost(bitmap.height)
        for (i in 0 until w) {
            for (j in 0 until h) {
                val color = bitmap.getPixel(i, j)
                // Calculate gray value (RGB -> YUV)
                if (isBright(color)) {
                    hitCount++
                }
            }
        }
        return hitCount / (w * h) > 0.5
    }

    private fun isBright(color: Int): Boolean {
        val grayLevel =
            ((Color.red(color) * 30 + Color.green(color) * 59 + Color.blue(color) * 11) / 100).toDouble()
        return grayLevel >= 215
    }

    private fun scaleBitmap(origin: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val height = origin.height
        val width = origin.width
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight) // 使用后乘
        return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false)
    }

    private fun scaleBitmapDown(bitmap: Bitmap): Bitmap {
        val minDimension = bitmap.width.coerceAtMost(bitmap.height)
        return if (minDimension <= SCALE_RATIO_FACTOR) {
            bitmap
        } else {
            val scaleRatio = SCALE_RATIO_FACTOR / minDimension.toFloat()
            Bitmap.createScaledBitmap(
                bitmap,
                (bitmap.width.toFloat() * scaleRatio).roundToInt(),
                (bitmap.height.toFloat() * scaleRatio).roundToInt(), false
            )
        }
    }

    /**
     * bitmap添加蒙层
     * @param old 原始图像
     * @param color 蒙层颜色
     * @return 新bitmap
     */
    fun drawCover(old: Bitmap, color: Int): Bitmap {
        val newBit = old.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(newBit)
        canvas.drawColor(color)
        return newBit
    }

}