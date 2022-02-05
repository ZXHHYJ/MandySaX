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
    blurBitmap = scaleBitmap(blurBitmap, blurBitmap.height * 150 / blurBitmap.width)
    blurBitmap = meshBitmap(blurBitmap, floats)
    blurBitmap = Toolkit.blur(blurBitmap, 25)
    return blurBitmap
}

var floats = floatArrayOf(
    -0.2351f,
    -0.0967f,
    0.2135f,
    -0.1414f,
    0.9221f,
    -0.0908f,
    0.9221f,
    -0.0685f,
    1.3027f,
    0.0253f,
    1.2351f,
    0.1786f,
    -0.3768f,
    0.1851f,
    0.2f,
    0.2f,
    0.6615f,
    0.3146f,
    0.9543f,
    0.0f,
    0.6969f,
    0.1911f,
    1.0f,
    0.2f,
    0.0f,
    0.4f,
    0.2f,
    0.4f,
    0.0776f,
    0.2318f,
    0.6f,
    0.4f,
    0.6615f,
    0.3851f,
    1.0f,
    0.4f,
    0.0f,
    0.6f,
    0.1291f,
    0.6f,
    0.4f,
    0.6f,
    0.4f,
    0.4304f,
    0.4264f,
    0.5792f,
    1.2029f,
    0.8188f,
    -0.1192f,
    1.0f,
    0.6f,
    0.8f,
    0.4264f,
    0.8104f,
    0.6f,
    0.8f,
    0.8f,
    0.8f,
    1.0f,
    0.8f,
    0.0f,
    1.0f,
    0.0776f,
    1.0283f,
    0.4f,
    1.0f,
    0.6f,
    1.0f,
    0.8f,
    1.0f,
    1.1868f,
    1.0283f
)

fun meshBitmap(old: Bitmap, floats: FloatArray): Bitmap {
    val fArr2 = FloatArray(72)
    var i = 0
    while (i <= 5) {
        var i2 = 0
        var i3 = 5
        while (i2 <= i3) {
            val i4 = i * 12 + i2 * 2
            val i5 = i4 + 1
            fArr2[i4] = floats[i4] * old.width.toFloat()
            fArr2[i5] = floats[i5] * old.height.toFloat()
            i2++
            i3 = 5
        }
        i++
    }
    val newBit = Bitmap.createBitmap(old)
    val canvas = Canvas(newBit)
    canvas.drawBitmapMesh(newBit, 5, 5, fArr2, 0, null, 0, null)
    return newBit
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
    matrix.postScale(scaleWidth, scaleHeight)//使用后乘
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