package studio.mandysa.music.logic.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import com.google.android.renderscript.Toolkit
import studio.mandysa.music.R
import kotlin.math.roundToInt


/**
 * @author ZXHHYJ
 */
fun handleImageBlur(context: Context, image: Bitmap): Bitmap {
    var blurBitmap = if (isWallpaperBright(image)) drawCover(
        image,
        context.getColor(R.color.translucent_black)
    ) else image
    val w = 150
    blurBitmap =
        scaleBitmap(blurBitmap, blurBitmap.height * w / blurBitmap.width)
    blurBitmap = Toolkit.blur(blurBitmap, 25)
    blurBitmap = Toolkit.blur(blurBitmap, 24)
    return blurBitmap
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

private fun scaleBitmap(origin: Bitmap, newHeight: Int): Bitmap {
    val height = origin.height
    val width = origin.width
    val scaleWidth = 150f / width
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