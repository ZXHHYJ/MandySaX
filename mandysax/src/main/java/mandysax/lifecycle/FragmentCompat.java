package mandysax.lifecycle;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import mandysax.core.annotation.Annotation;
import mandysax.core.annotation.BindLayoutId;
import mandysax.core.annotation.BindView;
import mandysax.design.FragmentPage;
import mandysax.utils.FieldUtils;

public class FragmentCompat extends Fragment implements LifecycleOwner,FragmentCompatImpl
{
	private final Lifecycle mLifecycle = new Lifecycle();

	private FragmentPage mPage;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DataEnum.LIFECYCLE.put(getClass().getCanonicalName(), mLifecycle);
		mLifecycle.onCreate();
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return mLifecycle;
	}

	@Override
	public void setFragmentPage(FragmentPage page)
	{
		if (this.mPage == null)
			this.mPage = page;
	}

	@Override
	public void startFragment(Class fragment)
	{
		if (mPage != null)mPage.startFragment(fragment);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return init(this);
	}

	@Override
	public void onStart()
	{
		super.onStart();
		mLifecycle.onStart();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		mLifecycle.onStop();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mLifecycle.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mLifecycle.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mLifecycle.onDestory();
		DataEnum.LIFECYCLE.remove(getClass().getCanonicalName());
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		mPage=null;
	}

	private final static View init(final Fragment mFragment)
	{
		final Class cls = mFragment.getClass();
	    View view = null;
		if (cls.isAnnotationPresent(BindLayoutId.class))	
			view = mFragment.getLayoutInflater().inflate(((BindLayoutId)cls.getAnnotation(BindLayoutId.class)).value(), null);
		if (view != null)
			for (Field field : cls.getDeclaredFields())
				if (field.isAnnotationPresent(BindView.class))	
					try
					{			
						BindView fvbi = field.getAnnotation(BindView.class);					
						if (fvbi.toString().equals(Annotation.BINDVIEW))
						{	
							FieldUtils.setField(field, mFragment, view.findViewById(Class.forName(mFragment.getContext().getPackageName() + ".R$id").newInstance().getClass().getDeclaredField(field.getName()).getInt(field.getName())));	
						}
						else
						{
							FieldUtils.setField(field, mFragment, view.findViewById(fvbi.value()));
						}
					}
					catch (Exception e)
					{
					}		
		return view;
	}

}
