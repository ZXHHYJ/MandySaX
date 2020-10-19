package mandysax.design;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import mandysax.R;
import mandysax.core.util.Xlist;
import mandysax.lifecycle.Fragment;
import mandysax.lifecycle.FragmentActivity;
import mandysax.lifecycle.FragmentMannger;
import mandysax.lifecycle.Lifecycle;
import mandysax.lifecycle.LifecycleObserver;

public class FragmentPage extends RelativeLayout
{
	private final static int ANIM_TIME=320;//动画时长
	//private final List<Fragment> return_stack=new ArrayList<Fragment>();//返回栈

	private final Xlist<Fragment> fragment_list=new Xlist<Fragment>();//主fragment

	private final LinearLayout top_viewgroup;

	private final FrameLayout bottom_viewgroup;

	private BottomNavigationBar bottom_bar;

	public FragmentPage(Context p0, AttributeSet p1)
	{
		super(p0, p1);
		inflate(getContext(), R.layout.fragment_page, this);
		top_viewgroup = findViewById(R.id.fragmentpageLinearLayout1);
		bottom_viewgroup = findViewById(R.id.fragmentpageFrameLayout2);
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
					top_viewgroup.addView(child);
				}
				if (lp.bottom)
				{
					removeView(child);
					bottom_viewgroup.addView(child);
					bottom_bar.bringToFront();
				}
			}
		}
	}

	public void add(Class... fragmentClass)
	{
		for (Class _fragmentClass:fragmentClass)
		{
			Fragment fragment= initFragment(_fragmentClass);
			if (fragment instanceof Fragment)
			{
				fragment.setFragmentPage(this);
			}
			if (fragment != null)
			{
				fragment_list.add(fragment);
			}
			else throw new NullPointerException("Could not initialize fragment:" + _fragmentClass.getCanonicalName());
		}
	}

	private Fragment initFragment(Class _fragmentClass)
	{
		try
		{
			Fragment fragment=((FragmentActivity)getContext()).findFragmentByTag(_fragmentClass.getCanonicalName());
			if (fragment == null)fragment = (Fragment) Class.forName(_fragmentClass.getCanonicalName()).newInstance();
			if (!fragment.isAdded())
				((FragmentActivity)getContext()).getMannger().add(R.id.fragmentpageFrameLayout1, fragment, _fragmentClass.getCanonicalName()).hide(fragment);	
			return fragment;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public void startFragment(final Fragment oldfragment, Class fragment)
	{
		/*
		 动画执行
		 */
		try
		{
			final Fragment startfragment=(Fragment) fragment.newInstance();
			if (startfragment == null)return;
			((FragmentActivity)getContext()).getMannger().add(getId(), startfragment).show(startfragment).addToBackStack();
			startfragment.getLifecycle().addObsever(new LifecycleObserver(){

					@Override
					public void Observer(Lifecycle.Event State)
					{
						if (State == Lifecycle.Event.ON_DESTORY)
						{
							/*
							 动画执行
							 */
							TranslateAnimation top = new TranslateAnimation(0, 0, bottom_viewgroup.getHeight(), 0);									
							top.setDuration(ANIM_TIME);
							top.setAnimationListener(new Animation.AnimationListener() {
									@Override
									public void onAnimationStart(Animation animation)
									{
									}
									@Override
									public void onAnimationRepeat(Animation animation)
									{
									}
									@Override
									public void onAnimationEnd(Animation animation)
									{			
									}
								});     
							bottom_viewgroup.setVisibility(View.VISIBLE);
							bottom_viewgroup.setAnimation(top);
							top_viewgroup.setAnimation(top);
							/*
							 动画执行
							 */
							oldfragment.onFragmentResult(startfragment.getIntent());
						}
					}
				});
		}
		catch (InstantiationException e)
		{
			throw new InstantiationError("Cannot instantiate " + fragment.getName());
		}
		catch (IllegalAccessException e)
		{
			throw new IllegalAccessError("Illegal access to " + fragment.getName());
		}
		
		/*
		 动画执行
		 */
		TranslateAnimation bottom = new TranslateAnimation(0, 0, 0, bottom_viewgroup.getHeight());
		bottom.setDuration(ANIM_TIME);
		bottom.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation)
				{					
					//return_stack.add(startFragment);
				}
				@Override
				public void onAnimationRepeat(Animation animation)
				{
				}
				@Override
				public void onAnimationEnd(Animation animation)
				{
					bottom_viewgroup.setVisibility(View.GONE);
				}
			});     
		bottom_viewgroup.setAnimation(bottom);
		top_viewgroup.setAnimation(bottom);
		/*
		 动画执行
		 */
	}

	public void showFragment(int index)
	{
		//if (return_stack.isEmpty())
		if (index < fragment_list.size())
		{

			FragmentMannger mannger = ((FragmentActivity)getContext()).getMannger();
			for (int i=0;i < fragment_list.size();i++)
			{
				if (!fragment_list.get(i).isHidden())
				{
					mannger.hide(fragment_list.get(i));
				}
			}	
			mannger.show(fragment_list.get(index));
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
