package mandysax.design;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import mandysax.*;

public class MusicView extends LinearLayout
{
    private ImageView iv1,iv2;
    private TextView t1;
	private ProgressBar pb1;
	private boolean play=false;

    public MusicView(Context p0)
    {
        super(p0);
        init();
    }

    public MusicView(Context p0, AttributeSet p1)
    {
        super(p0, p1);
        init();
    }

    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.music_view, this);
        iv1 = findViewById(R.id.mandysamusicviewImageView1);
        iv2 = findViewById(R.id.mandysamusicviewImageView2);
        t1 = findViewById(R.id.mandysamusicviewTextView1);
		pb1 = findViewById(R.id.musicviewProgressBar1);
		iv2.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (play)
					{
						playmode();
					}
					else stopmode();
					play = !play;
				}
			});
	}

	public ImageView getPlaybutton()
	{
		return iv2;
	}

	public void playmode()
	{
		iv2.setImageResource(R.mipmap.ic_pause);
		if (iv2.getVisibility() == View.GONE)
		{
			iv2.setVisibility(View.VISIBLE);
			pb1.setVisibility(View.GONE);
		}

	}

	public void stopmode()
	{
		iv2.setImageResource(R.mipmap.ic_play);
		if (iv2.getVisibility() == View.GONE)
		{
			iv2.setVisibility(View.VISIBLE);
			pb1.setVisibility(View.GONE);
		}
	}

	public void loadmode()
	{
		iv2.setVisibility(View.GONE);
		pb1.setVisibility(View.VISIBLE);
	}

	public void setTitle(String title)
	{
		t1.setText(title);
	}

	public ImageView getImgView()
	{
		return iv1;
	}
}
