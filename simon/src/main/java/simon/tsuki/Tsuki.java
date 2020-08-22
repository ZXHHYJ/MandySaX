package simon.tsuki;
import android.widget.ImageView;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import android.os.Handler;
import android.os.Looper;
import android.graphics.Bitmap;

public class Tsuki
{
    
    private static volatile List<TsukiTask> tasks=new CopyOnWriteArrayList<TsukiTask>();
    private static volatile int judge,tusknum=3;
    private static Handler mainhandler=new Handler(Looper.getMainLooper());
    private static boolean isstart;
    public static TsukiTask load(String url){
        return new TsukiTask(url);
    }
    public static void setMaxThreadNum(int num){
        tusknum=num;
    }
    public TsukiTask one;
    public Tsuki(TsukiTask task){
        one=task;
    }
    public void start(){
        tasks.add(one);
        if(!isstart)
             main();
    }
    private static void main(){
        isstart=true;
        new Thread(new Runnable(){
                @Override
                public void run()
                {
                    while(judge>=tusknum){}
                    if(tasks.size()!=0){
                        doo(tasks.get(0));  
                        tasks.remove(0);
                        judge++;
                    }
                    main();
                }
            }).start();   
    }
    private static void doo(final TsukiTask in){
        new Thread(new Runnable(){

                @Override
                public void run()
                {
                    final Bitmap bit=in.getEdit().EditBitmap(in.engine.getBitmap(in),in);
                    mainhandler.post(new Runnable(){
                            @Override
                            public void run()
                            {
                                in.getImg().setImageBitmap(bit);
                                judge--;
                            }
                        });
                }
            }).start();
    }
   public static class TsukiTask{
        private String url;
        private ImageView img;
        private Bitmap errorBitmap;
        private Engine engine;
        private Editor edit;
        public TsukiTask(String one){
            url=one;
            engine=new CommonEngine(true,true);
            edit=new CommonEdit();
            errorBitmap=Bitmap.createBitmap(1,1,Bitmap.Config.ARGB_4444);
        }
        public <T extends Editor> TsukiTask setEdit(T edit)
        {
            this.edit = edit;
            return this;
        }
        public Editor getEdit()
        {
            return edit;
        }
        public <T extends Engine> TsukiTask setEngine(T engine)
        {
            this.engine = engine;
            return this;
        }
        public Engine getEngine()
        {
            return engine;
        }
        public TsukiTask setErrorBitmap(Bitmap errorBitmap)
        {
            this.errorBitmap = errorBitmap;
            return this;
        }
        public Bitmap getErrorBitmap()
        {
            return errorBitmap;
        }
        public String getUrl()
        {
            return url;
        }
        public Tsuki setImg(ImageView img)
        {
            this.img = img;
            return new Tsuki(this);
        }
        public ImageView getImg()
        {
            return img;
        }
   }
  public interface Engine{
       Bitmap getBitmap(TsukiTask task)
   }
  public interface Editor{
      Bitmap EditBitmap(Bitmap oldBit,TsukiTask task)
  }
}
