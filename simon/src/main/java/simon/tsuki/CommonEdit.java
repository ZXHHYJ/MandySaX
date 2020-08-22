package simon.tsuki;
import simon.tsuki.Tsuki.TsukiTask;
import android.graphics.Bitmap;

public class CommonEdit implements Tsuki.Editor
{

    @Override
    public Bitmap EditBitmap(Bitmap old,Tsuki.TsukiTask task)
    {
        return BitmapUtils.zoomImg(old,task.getImg().getWidth(),task.getImg().getHeight());
    }
    
}
