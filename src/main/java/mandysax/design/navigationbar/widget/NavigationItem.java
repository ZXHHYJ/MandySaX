package mandysax.design.navigationbar.widget;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import mandysax.R;
import mandysax.utils.DensityUtils;

public class NavigationItem extends LinearLayout
{

    private TextView textView;

    private ImageView imageView;

    private int checked=R.color.default_checked_color;

    private int unchecked=R.color.default_unchecked_color;

	private int checkedResource;

	private int uncheckedResource;

    private int index;

	private boolean state;

    public NavigationItem(int index, String text, Context context, int checkedResource, int uncheckedResource)
	{
        super(context);
        this.index = index;
		this.checkedResource = checkedResource;
		this.uncheckedResource = uncheckedResource;
		setGravity(Gravity.CENTER);
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
        setOrientation(VERTICAL);
		if (!(checkedResource == 0 || checkedResource == 0))
		{
			imageView = new ImageView(context);
			imageView.setImageResource(uncheckedResource);
			addView(imageView);
		}
		if (text != null)
		{
			textView = new TextView(context);
			textView.setText(text);
			textView.setTextColor(getContext().getColor(unchecked));
			textView.setTextSize(12);
			textView.setPadding(0, DensityUtils.dip2px(context, 1), 0, 0);
			textView.setGravity(Gravity.CENTER);
			addView(textView);
		}
    }
	
	public void setTextColor(int checked,int unchecked){
		if(checked!=0)
		this.checked=checked;
		if(unchecked!=0)
		this.unchecked=unchecked;
		textView.setTextColor(getContext().getColor(state ?this.checked: this.unchecked));
	}

    public int getIndex()
    {
        return index;
    }

    public void check()
	{
		state = !state;
		if (imageView != null)
			imageView.setImageResource(state ?checkedResource: uncheckedResource);
		if (textView != null)
		{
			textView.getPaint().setFakeBoldText(state); 
			textView.setTextColor(getContext().getColor(state ?checked: unchecked));
		}
	}

}
