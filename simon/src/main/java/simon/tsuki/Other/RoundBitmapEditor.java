package simon.tsuki.Other;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import simon.tsuki.Tsuki;

public class RoundBitmapEditor implements Tsuki.Editor
{
    private int radious;
    public RoundBitmapEditor(int radious){
      this.radious=radious;
    }
    @Override
    public Bitmap EditBitmap(Bitmap mBitmap, Tsuki.TsukiTask task)
    {
        Bitmap bitmap =Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0,0,mBitmap.getWidth(),mBitmap.getHeight());
        RectF rectf = new RectF(rect);
        canvas.drawRoundRect(rectf, radious, radious, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, rect, rect, paint);
        return bitmap;
    }
    
}
