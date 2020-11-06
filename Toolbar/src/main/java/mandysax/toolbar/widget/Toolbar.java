package mandysax.toolbar.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import mandysax.toolbar.R;

public class Toolbar extends FrameLayout
{
	private TextView title;

    public Toolbar(Context context)
    {
        super(context);
        init();
    }

    public Toolbar(Context context, AttributeSet attrs)
    {
        super(context, attrs); 		
        init();
		if (attrs != null)
		{
			title.setText(context.obtainStyledAttributes(attrs, R.styleable.Toolbar).getString(R.styleable.Toolbar_android_name));
		}
    }

	@Override
	protected void dispatchDraw(Canvas canvas)
	{
		super.dispatchDraw(canvas);
	}
	
	
    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.toolbar, this);   
		title = findViewById(R.id.toolbarTitle);
		title.setText(((Activity)getContext()).getTitle());
    }

	public void setTitle(CharSequence title)
	{
		this.title.setText(title);
	}
}
