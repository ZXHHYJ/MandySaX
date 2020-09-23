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
import mandysax.lifecycle.FragmentCompat;

public class FragmentPage extends RelativeLayout
{

	private final List<Fragment> list=new ArrayList<Fragment>();;

	private BottomNavigationBar bottom_bar;

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

	public List<Fragment> add(Fragment... list)
	{
		for (Fragment fragment:list)
		{
			if(fragment == null)throw new NullPointerException("Fragment not null!");
			this.list.add(fragment);
			if (fragment instanceof FragmentCompat)((FragmentCompat)fragment).setFragmentPage(this);
		}
		return this.list;
	}

	public List<Fragment> add(Fragment... list, int index)
	{
		add(list);
		showFragment(index);
		return this.list;
	}

	public void showFragment(int index)
	{
		if(index<list.size()){
		final FragmentTransaction transaction =((Activity) getContext()).getFragmentManager().beginTransaction();
		for (int i=0;i < list.size();i++)
		{
			if (!list.get(i).isHidden())
			{
				transaction.hide(list.get(i));
			}
		}
		transaction.show(list.get(index));
		transaction.commit();
		}else throw new  ArrayIndexOutOfBoundsException("index > Fragment List!");
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

	public static class LayoutParams extends RelativeLayout.LayoutParams
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

