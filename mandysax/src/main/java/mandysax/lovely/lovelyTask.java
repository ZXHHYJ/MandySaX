package mandysax.lovely;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import mandysax.Lifecycle.LifecycleAbstract;
import mandysax.Lifecycle.LiveData.MutableLiveData;
import mandysax.Lifecycle.LiveData.Observer;
import mandysax.Tools.BitmapUtils;

public class lovelyTask
{

	private final MutableLiveData<Bitmap> callback = new MutableLiveData<Bitmap>();

	private String url;

	private Drawable error_img;

	private boolean zoom=false;

	//private int blur=0;

	private ImageView view;

	public lovelyTask(final LifecycleAbstract p0)
	{
		callback.observe(p0, new Observer<Bitmap>(){

				@Override
				public void onChanged(Bitmap p1)
				{
					if(view!=null)
					if (p1 != null)
					{	
						if (zoom)
						{			
							view.setImageBitmap(BitmapUtils.zoomImg(p1, view.getWidth(), view.getHeight()));
						}
						else
						{
							view.setImageBitmap(p1);
						}
					}
					else
					{
						if (error_img != null)
							view.setImageDrawable(error_img);
					}
				}
			});
	}

	public lovelyTask load(String p0)
	{
		this.url = p0;
		return this;
	}

	public lovelyTask zoom(boolean p0)
	{
		this.zoom = p0;
		return this;
	}

	public lovelyTask error(Drawable p0)
	{
		this.error_img = p0;
		return this;
	}

	/*public lovelyTask blur(int p0)
	 {
	 this.blur = p0;
	 return this;
	 }*/

	public Bitmap getBitmap()
	{
		return lovely.get(this);
	}

	public String getUrl()
	{
		return url;
	}

	public void into(ImageView view)
	{
		this.view = view;
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					callback.postValue(lovely.get(lovelyTask.this));				
				}
			}).start();
	}  	
}
