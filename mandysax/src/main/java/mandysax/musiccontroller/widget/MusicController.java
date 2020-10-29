package mandysax.musiccontroller.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import mandysax.R;

public class MusicController extends FrameLayout
{
    private ImageView cover,playButton;
    private TextView title;
	private ProgressBar loading;
	private boolean play=false;

    public MusicController(Context context)
    {
        super(context);
        init();
    }

    public MusicController(Context context, AttributeSet attrs)
    {
        super(context, attrs); 
        init();
        if (attrs != null)
        {
            title.setText(context.obtainStyledAttributes(attrs, R.styleable.MusicController).getString(R.styleable.MusicController_textTint));
        }

    }
    private void init()
    {
		//setClipChildren(false);
        LayoutInflater.from(getContext()).inflate(R.layout.music_controller, this);
        cover = findViewById(R.id.musicviewCover);
        playButton = findViewById(R.id.musicviewPlay);
        title = findViewById(R.id.musicviewTitle);
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

	public void setTitle(String title)
	{
		if (title == null) throw new NullPointerException("String not null!");
		this.title.setText(title);
	}

	public ImageView getImgView()
	{
		return cover;
	}
}
