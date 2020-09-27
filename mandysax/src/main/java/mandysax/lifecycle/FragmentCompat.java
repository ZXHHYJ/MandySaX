package mandysax.lifecycle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build.VERSION;
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
	private final Lifecycle lifecycle = new Lifecycle();

	private FragmentPage page;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DataEnum.LIFECYCLE.put(getClass().getCanonicalName(), lifecycle);
	}
	
	@Override
	public Lifecycle getLifecycle()
	{
		return lifecycle;
	}

	@Override
	public void setFragmentPage(FragmentPage page)
	{
		if(page==null)
		this.page=page;
	}

	@Override
	public void startFragment(Class fragment)
	{
		if(page!=null)page.startFragment(fragment);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return init(this);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if(VERSION.SDK_INT<23)//兼容低版本
					onAttach(activity);		
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (context instanceof AppCompatActivity)
			((AppCompatActivity) context).getLifecycle().addObsever(new LifecycleObserver(){

					@Override
					public void Observer(Lifecycle.Event State)
					{
						if (State == Lifecycle.Event.ON_KILL)
							FragmentCompat.this.lifecycle.onKill();
					}
				});
	}

	@Override
	public void onStart()
	{
		super.onStart();
		lifecycle.onStart();
	}

	@Override
	public void onStop()
	{
		super.onStop();
		lifecycle.onStop();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		lifecycle.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		lifecycle.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		lifecycle.onDestory();
		DataEnum.LIFECYCLE.remove(getClass().getCanonicalName());
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
