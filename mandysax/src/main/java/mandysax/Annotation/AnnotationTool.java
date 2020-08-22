package mandysax.Annotation;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.View.*;
import java.lang.reflect.*;

public class AnnotationTool 
{
	

	public static void init(final Activity mActivity)
	{
		Class cls = mActivity.getClass();
		Field[] fArray = cls.getDeclaredFields();
		if (cls.isAnnotationPresent(BindLayoutId.class))
		{
			try
			{  
                Method setContentViewMethod = cls.getMethod("setContentView", int.class);   
                setContentViewMethod.invoke(mActivity, ((BindLayoutId)cls.getAnnotation(BindLayoutId.class)).value());
            }
			catch ( Exception e )
			{  
				e.printStackTrace();
            }
		}

		for (Field field : fArray)
		{
			if (field.isAnnotationPresent(BindView.class))
			{
				BindView fvbi = field.getAnnotation(BindView.class);
				try
				{
					field.setAccessible(true);
					field.set(mActivity, mActivity.findViewById(fvbi.value()));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		Method[] mArray = cls.getMethods();
		for (final Method method : mArray)
		{
			if (method.isAnnotationPresent(ViewClick.class))
			{
				ViewClick vc = method.getAnnotation(ViewClick.class);
				mActivity.findViewById(vc.value()).setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View p1)
						{
							try
							{
								method.invoke(mActivity);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					});
			}
			if (method.isAnnotationPresent(ViewLongClick.class))
			{
				ViewLongClick vc = method.getAnnotation(ViewLongClick.class);
				mActivity.findViewById(vc.value()).setOnLongClickListener(new OnLongClickListener(){
						@Override
						public boolean onLongClick(View p1)
						{
							try
							{
								method.invoke(mActivity);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							return true;
						}

					});
			}
			if (method.isAnnotationPresent(RunThread.class))
			{
				new Thread(new Runnable(){
						@Override
						public void run()
						{
							try
							{
								method.invoke(mActivity);
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}).start();
			}
		}
	}
}

