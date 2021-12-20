package studio.mandysa.music.logic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import androidx.annotation.NonNull;

/**
 * @author ZXHHYJ
 */
public final class BitmapUtil {

    public static Bitmap handleImageEffect(@NonNull Bitmap bm, float saturation) {
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(saturationMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        Bitmap bitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bm, 0, 0, paint);
        bm.recycle();
        return bitmap;
    }

    public static Bitmap doBlur(Context context, Bitmap sentBitmap, float radius, float ty) {
        if (sentBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createScaledBitmap(sentBitmap, ((int) (sentBitmap.getWidth() / ty)), ((int) (sentBitmap.getHeight() / ty)), false);
        //先缩放图片，增加模糊速度
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        sentBitmap.recycle();
        rs.destroy();
        return bitmap;
    }
}
