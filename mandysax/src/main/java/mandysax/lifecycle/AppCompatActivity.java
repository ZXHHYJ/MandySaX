package mandysax.lifecycle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mandysax.R;
import mandysax.core.annotation.Annotation;
import mandysax.core.annotation.BindFragment;
import mandysax.core.annotation.BindLayoutId;
import mandysax.core.annotation.BindView;
import mandysax.design.FragmentPage;
import mandysax.utils.FieldUtils;

public class AppCompatActivity extends FragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		init(this);
	}

	@Override
	public void startActivity(Intent intent)
	{
		super.startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent, Bundle options)
	{
		super.startActivity(intent, options);
	}

	private final static void init(final Activity mActivity)
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
					if (fvbi.toString().equals(Annotation.BINDVIEW))
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
					android.app.Fragment fragment=mActivity.getFragmentManager().findFragmentByTag(field.getName());
					if (fragment == null)
					{
						fragment = (android.app.Fragment) Class.forName(field.getType().getName()).newInstance();
						FieldUtils.setField(field, mActivity, fragment);
						if (!fragment.isAdded() && mActivity.findViewById(ffbt.value()) instanceof FragmentPage)
							mActivity.getFragmentManager().beginTransaction().add(R.id.fragmentpageFrameLayout1, fragment, field.getName()).hide(fragment).commit();		
						else
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
	}

}
