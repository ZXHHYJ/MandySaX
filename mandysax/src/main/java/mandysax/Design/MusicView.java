package mandysax.Design;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import mandysax.R;

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
		iv2.setImageResource(R.drawable.twotone_pause_black_48dp);
		if (iv2.getVisibility() == View.GONE)
		{
			iv2.setVisibility(View.VISIBLE);
			pb1.setVisibility(View.GONE);
		}

	}

	public void stopmode()
	{
		iv2.setImageResource(R.drawable.twotone_play_arrow_black_48dp);
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
