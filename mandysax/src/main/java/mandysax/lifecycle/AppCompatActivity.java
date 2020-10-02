package mandysax.lifecycle;
import android.app.Activity;
import android.app.Fragment;
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

public class AppCompatActivity extends Activity implements LifecycleOwner
{

	private final Lifecycle mLifecycle = new Lifecycle();

	private ViewModelStore mViewModelStore;

	private NonConfigurationInstances mLastNonConfigurationInstances;

	public ViewModelStore getViewModelStore()
	{
        if (getApplication() != null)
		{
            if (mViewModelStore == null)
			{
                NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
                if (nonConfigurationInstances != null)
                    mViewModelStore = nonConfigurationInstances.viewModelStore; 
                if (mViewModelStore == null)
                    mViewModelStore = new ViewModelStore(); 
            } 
            return this.mViewModelStore;
        } 
        throw new IllegalStateException("Your activity is not yet attached to the Application instance. You can't request ViewModel before onCreate call.");
    }

	@Override
	public final Object onRetainNonConfigurationInstance()
	{
		if (mLastNonConfigurationInstances == null)
		{
			NonConfigurationInstances nci = new NonConfigurationInstances();
			nci.viewModelStore = mViewModelStore;
			return nci;
		}
		else return mLastNonConfigurationInstances;
	}

	@Override
	public Object getLastNonConfigurationInstance()
	{
		return super.getLastNonConfigurationInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);	
		init(this);
		DataEnum.LIFECYCLE.put(getClass().getCanonicalName(), mLifecycle);
		mLifecycle.onCreate();
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return mLifecycle;
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mLifecycle.onStart();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		mLifecycle.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mLifecycle.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mLifecycle.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mLifecycle.onDestory();
		if (!isChangingConfigurations())
			mViewModelStore.clear();
		DataEnum.LIFECYCLE.remove(getClass().getCanonicalName());		
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
					Fragment fragment=mActivity.getFragmentManager().findFragmentByTag(field.getName());
					if (fragment == null)
					{
						fragment = (Fragment) Class.forName(field.getType().getName()).newInstance();
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
	static final class NonConfigurationInstances
	{
		ViewModelStore viewModelStore;
	}
}

