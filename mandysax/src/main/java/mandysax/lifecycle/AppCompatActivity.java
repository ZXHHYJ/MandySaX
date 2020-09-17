package mandysax.lifecycle;
import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import java.lang.reflect.*;
import mandysax.core.annotation.*;
import mandysax.utils.*;

public class AppCompatActivity extends Activity implements LifecycleOwner
{

	private final Lifecycle lifecycle = new Lifecycle();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init(this);
		DataEnum.LIFECYCLE.put(getClass().getCanonicalName(), lifecycle);
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return lifecycle;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		lifecycle.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		lifecycle.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		lifecycle.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		lifecycle.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		lifecycle.onDestory();
		if (!isChangingConfigurations())
			lifecycle.onKill();
		DataEnum.LIFECYCLE.remove(getClass().getCanonicalName());		
	}

	private static void init(final Activity mActivity)
	{
		Class cls = mActivity.getClass();
		if (cls.isAnnotationPresent(BindLayoutId.class))
			try
			{  
                Method setContentViewMethod = cls.getMethod("setContentView", int.class);   
                setContentViewMethod.invoke(mActivity, ((BindLayoutId)cls.getAnnotation(BindLayoutId.class)).value());
            }
			catch ( Exception e )
			{  
            }
		for (Field field : cls.getDeclaredFields())
			if (field.isAnnotationPresent(BindView.class))
			{
				BindView fvbi = field.getAnnotation(BindView.class);
				try
				{	
					if (fvbi.toString().equals("@mandysax.core.annotation.BindView(value=NO_VALUE)"))
					{	
					FieldUtils.setField(field, mActivity, mActivity.findViewById(Class.forName(mActivity.getPackageName() + ".R$id").newInstance().getClass().getDeclaredField(field.getName()).getInt(field.getName())));	
					}
					else
					{
						FieldUtils.setField(field, mActivity, mActivity.findViewById(fvbi.value()));
					}		
				}
				catch (Exception e)
				{
				}
			}
			else 
			if (field.isAnnotationPresent(BindFragment.class))
				try
				{
					BindFragment ffbt = field.getAnnotation(BindFragment.class);		
					Fragment fragment=mActivity.getFragmentManager().findFragmentByTag(field.getName());
					if (fragment == null)
					{
						fragment = (Fragment) Class.forName(field.getType().getName()).newInstance();
						FieldUtils.setField(field, mActivity, fragment);
						if (!fragment.isAdded())
							mActivity.getFragmentManager().beginTransaction().add(ffbt.value(), fragment, field.getName()).hide(fragment).commit();		
					}
					else
					{
						FieldUtils.setField(field, mActivity, fragment);
					}			
				}
				catch (Exception e)
				{
				}

		for (final Method method : cls.getMethods())
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
			else
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
			else
			if (method.isAnnotationPresent(RunThread.class))
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

