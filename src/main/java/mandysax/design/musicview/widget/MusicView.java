package mandysax.design.musicview.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import mandysax.R;

public class MusicView extends FrameLayout
{
    private ImageView cover,playButton;

    private TextView title,subTitle;

	private ProgressBar loading;

	private boolean play=false;

    public MusicView(Context context)
    {
        super(context);
        init();
    }

    public MusicView(Context context, AttributeSet attrs)
    {
        super(context, attrs); 
        init();
        if (attrs != null)
        {
            title.setText(context.obtainStyledAttributes(attrs, R.styleable.MusicView).getString(R.styleable.MusicView_textTint));
        }

    }
    private void init()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.music_view, this);
        cover = findViewById(R.id.musicviewCover);
        playButton = findViewById(R.id.musicviewPlay);
        title = findViewById(R.id.musicviewTitle);
		subTitle = findViewById(R.id.musicviewSubTitle);
        loading = findViewById(R.id.musicviewLoading);
        playButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1)
                {
                    if (play)
                    {
                        playMode();
                    }
                    else stopMode();
                    play = !play;
                }
			});
    }

	public ImageView getPlaybutton()
	{
		return playButton;
	}
	
	public void playMode()
	{
		playButton.setImageResource(R.mipmap.ic_pause);
		if (playButton.getVisibility() == View.GONE)
		{
			playButton.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
		}

	}

	public void stopMode()
	{
		playButton.setImageResource(R.mipmap.ic_play);
		if (playButton.getVisibility() == View.GONE)
		{
			playButton.setVisibility(View.VISIBLE);
			loading.setVisibility(View.GONE);
		}
	}

	public void loadMode()
	{
		playButton.setVisibility(View.GONE);
		loading.setVisibility(View.VISIBLE);
	}

	public MusicView setTitle(String title)
	{
		this.title.setText(title);
		return this;
	}
	
	public MusicView setSubTitle(String title){
		subTitle.setText(title);
		return this;
	}

	public ImageView getCoverView()
	{
		return cover;
	}
}
