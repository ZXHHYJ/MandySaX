package mandysax.lifecycle;
import android.app.*;
import android.os.*;
import android.view.*;
import java.lang.reflect.*;
import mandysax.core.annotation.*;
import mandysax.utils.*;

public abstract class FragmentCompat extends Fragment implements LifecycleOwner
{

	private final Lifecycle lifecycle = new Lifecycle();

	{
		DataEnum.LIFECYCLE.put(getClass().getCanonicalName(), lifecycle);
	}

	@Override
	public Lifecycle getLifecycle()
	{
		return lifecycle;
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
		if (activity instanceof AppCompatActivity)
			((AppCompatActivity) activity).getLifecycle().addObsever(new LifecycleObserver(){

					@Override
					public void Observer(int State)
					{
						if (State == 0)
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

	private static View init(final Fragment mFragment)
	{
		Class cls = mFragment.getClass();
	    View view = null;
		if (cls.isAnnotationPresent(BindLayoutId.class))	
			view = mFragment.getLayoutInflater().inflate(((BindLayoutId)cls.getAnnotation(BindLayoutId.class)).value(), null);
		if (view != null)
			for (Field field : cls.getDeclaredFields())
				if (field.isAnnotationPresent(BindView.class))	
					try
					{			
						BindView fvbi = field.getAnnotation(BindView.class);					
						if (fvbi.toString().equals("@mandysax.core.annotation.BindView(value=NO_VALUE)"))
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
