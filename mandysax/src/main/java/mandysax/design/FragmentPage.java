package mandysax.design;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;
import mandysax.R;
import mandysax.data.SafetyHashMap;
import mandysax.lifecycle.FragmentCompat;

public class FragmentPage extends RelativeLayout
{
	private int screenWidth;
    private float fractionX;
    private OnLayoutTranslateListener onLayoutTranslateListener;
	private String[] fragment_key;
	private final SafetyHashMap<String,Fragment> fragment_map=new SafetyHashMap<String,Fragment>();
	private BottomNavigationBar bottom_bar;

	private Activity getActivity()
	{
		return (Activity)getContext();
	}

	public FragmentPage(Context p0)
	{
		super(p0);
		init();
	}

	public FragmentPage(Context p0, AttributeSet p1)
	{
		super(p0, p1);
		init();
	}

	private void init()
	{
		inflate(getContext(), R.layout.fragment_page, this);
	}

	protected void onSizeChanged(int w, int h, int oldW, int oldH)
	{

        // Assign the actual screen width to our class variable.
        screenWidth = w;

        super.onSizeChanged(w, h, oldW, oldH);
    }

    public float getFractionX()
	{
        return fractionX;
    }

    public void setFractionX(float xFraction)
	{
        this.fractionX = xFraction;

        // When we modify the xFraction, we want to adjust the x translation
        // accordingly.  Here, the scale is that if xFraction is -1, then
        // the layout is off screen to the left, if xFraction is 0, then the
        // layout is exactly on the screen, and if xFraction is 1, then the
        // layout is completely offscreen to the right.
        setX((screenWidth > 0) ? (xFraction * screenWidth) : 0);

        if (xFraction == 1 || xFraction == -1)
		{
            setAlpha(0);
        }
		else if (xFraction < 1 /* enter */|| xFraction > -1)
		{
            if (getAlpha() != 1)
			{
                setAlpha(1);
            }
        }

        if (onLayoutTranslateListener != null)
		{
            onLayoutTranslateListener.onLayoutTranslate(this, xFraction);
        }
    }

    public void setOnLayoutTranslateListener(OnLayoutTranslateListener onLayoutTranslateListener)
	{
        this.onLayoutTranslateListener = onLayoutTranslateListener;
    }

    private static interface OnLayoutTranslateListener
	{
        void onLayoutTranslate(FragmentPage view, float xFraction);
    }

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
		for (int i = 0;i < getChildCount(); i++)
		{
			View child = getChildAt(i);
			if (bottom_bar == null && child instanceof BottomNavigationBar)
			{
				bottom_bar = (BottomNavigationBar) child;	
				removeView(bottom_bar);
				((FrameLayout)findViewById(R.id.fragmentpageFrameLayout2)).addView(bottom_bar);
			}
			else	
			if (checkLayoutParams(child.getLayoutParams()))
			{
				LayoutParams lp=((LayoutParams)child.getLayoutParams());
				if (lp.top)
				{	
					removeView(child);
					((LinearLayout)findViewById(R.id.fragmentpageLinearLayout1)).addView(child);
				}
				if (lp.bottom)
				{
					removeView(child);
					((FrameLayout)findViewById(R.id.fragmentpageFrameLayout2)).addView(child);
					bottom_bar.bringToFront();
				}
			}
		}
	}

	public void add(Class... fragmentClass,int index){
		add(fragmentClass);
		showFragment(index);
	}
	
	public void add(Class... fragmentClass)
	{
		final List<String> list=new ArrayList<String>();
		for (Class _fragmentClass:fragmentClass)
		{
			Fragment fragment= initFragment(_fragmentClass);
			if (fragment != null)
			{
				fragment_map.put(_fragmentClass.getCanonicalName(), fragment);
				list.add(_fragmentClass.getCanonicalName());
			}
			else throw new NullPointerException("Could not initialize fragment:" + _fragmentClass.getCanonicalName());
		}
		fragment_key = list.toArray(new String[0]);
	}

	private Fragment initFragment(Class _fragmentClass)
	{
		try
		{
			Fragment fragment=getActivity().getFragmentManager().findFragmentByTag(_fragmentClass.getCanonicalName());
			if (fragment == null)fragment = (Fragment) Class.forName(_fragmentClass.getCanonicalName()).newInstance();
			if (!fragment.isAdded())
				getActivity().getFragmentManager().beginTransaction().add(R.id.fragmentpageFrameLayout1, fragment, _fragmentClass.getCanonicalName()).hide(fragment).commit();		
			return fragment;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void startFragment(Class fragment)
	{
		Fragment startFragment=fragment_map.get(fragment);
		if (startFragment != null)
		{
			getActivity().
				getFragmentManager()
				.beginTransaction()
				.setCustomAnimations(
				R.anim.slide_right_in, R.anim.slide_left_out,
				R.anim.slide_left_in, R.anim.slide_right_out)
				//.replace(R.id.your_fragment, YourFragment.newInstance())
				.commit();
		}
		else throw new NullPointerException("Unable to startFragment:" + fragment.getName());
	}

	public void showFragment(int index)
	{
		if (index < fragment_key.length)
		{
			final FragmentTransaction transaction =getActivity().getFragmentManager().beginTransaction();
			for (int i=0;i < fragment_key.length;i++)
			{
				if (!fragment_map.get(fragment_key[i]).isHidden())
				{
					transaction.hide(fragment_map.get(fragment_key[i]));
				}
			}
			transaction.show(fragment_map.get(fragment_key[index]));
			transaction.commit();
		}
	    else throw new  ArrayIndexOutOfBoundsException("index > Fragment List!");
	}

	@Override
	public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new LayoutParams(getContext(), attrs);
	}
	@Override
	protected RelativeLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
	{
		return new LayoutParams(p);
	}
	@Override
	protected LayoutParams generateDefaultLayoutParams()
	{
		return new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
	}
	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
	{
		return p instanceof LayoutParams ;
	}

	private class LayoutParams extends RelativeLayout.LayoutParams
	{		
		public boolean show;
		public boolean top;
		public boolean bottom;

		public LayoutParams(Context context, AttributeSet object)
		{
			super(context, object);
			TypedArray array = context.obtainStyledAttributes(object, R.styleable.FragmentPage);
			show = array.getBoolean(R.styleable.FragmentPage_always_show, true);
			top = array.getBoolean(R.styleable.FragmentPage_navigationbar_top, false);
			bottom = array.getBoolean(R.styleable.FragmentPage_always_bottom, false);
			array.recycle();
		}

		public LayoutParams(int width, int height)
		{
			super(width, height);
		}

		public LayoutParams(android.view.ViewGroup.LayoutParams source)
		{
			super(source);
		}

		public LayoutParams(android.view.ViewGroup.MarginLayoutParams source)
		{
			super(source);
		}

		public LayoutParams(android.widget.FrameLayout.LayoutParams source)
		{
			super(source);
		}

	}

}

