package mandysax.design.navigationbar.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;

public class BottomNavigationBar extends LinearLayout implements OnClickListener
{

	public void setTextColor(int checked)
	{
		setTextColor(checked, 0);
	}


	public BottomNavigationBar setTextColor(int checked, int unchecked)
	{
		for (NavigationItem item:mItems)
		{
			item.setTextColor(checked, unchecked);
		}
		return this;
	}
    public BottomNavigationBar setOnItemViewSelectedListener(OnItemViewSelectedListener onItemViewSelectedListener)
    {
        this.onItemViewSelectedListener = onItemViewSelectedListener;
		return this;
    }

	/*public BottomNavigationBar setOnItemViewSelectedListener(final FragmentPage page)
	 {
	 onClick(getChildAt(page.getIndex()));
	 this.onItemViewSelectedListener = new OnItemViewSelectedListener(){
	 @Override
	 public void onItemClcik(int index)
	 {
	 page.showFragment(index);
	 }
	 };
	 return this;
	 }*/

    @Override
    public void onClick(View p1)
    {
        NavigationItem item=(NavigationItem) p1;  	
		if (mIndex != item.getIndex())
		{
			((NavigationItem)getChildAt(mIndex)).check();
			item.check();
			mIndex = item.getIndex();
		}
		if (onItemViewSelectedListener != null)
			onItemViewSelectedListener.onItemClcik(item.getIndex());
	}

    private int mIndex;

    private OnItemViewSelectedListener onItemViewSelectedListener;

    private final List<NavigationItem> mItems=new ArrayList<NavigationItem>();

    public BottomNavigationBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

	public BottomNavigationBar setChecked(int index)
	{
		onClick(getChildAt(index));
		return this;
	}

    public BottomNavigationBar addItem(String text)
	{
        addItem(text, 0, 0);
		return this;
    }

	public BottomNavigationBar addItem(int image, int image2)
	{
		addItem(null, image, image2);
		return this;
	}

	public BottomNavigationBar addItem(int textRes, int image, int image2)
	{
		addItem(getResources().getString(textRes), image, image2);
		return this;
    }

    public BottomNavigationBar addItem(String text, int image, int image2)
	{
        NavigationItem item=new NavigationItem(mItems.size(), text, getContext(), image, image2);
        item.setOnClickListener(this);
        mItems.add(item);
        addView(item);
		if (mItems.size() == 1)mItems.get(0).check();
		return this;
    }

    public abstract interface OnItemViewSelectedListener
	{
        public void onItemClcik(int index);
    }

}
