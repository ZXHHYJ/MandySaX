package com.FMJJ.MandySa;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.FMJJ.MandySa.*;
import com.FMJJ.MandySa.ViewModel.*;
import mandysax.Annotation.*;
import mandysax.Lifecycle.*;
import mandysax.Lifecycle.LiveData.*;
import mandysax.Lifecycle.ViewModel.*;
import simon.tuke.*;

@BindLayoutId(R.layout.start)
public class StartActivity extends LifecycleActivity implements PopupMenu.OnMenuItemClickListener
{
	@BindView(R.id.start_choose_mode)
	private TextView choose_mode;

	private final StartViewModel viewModel= ViewModelProviders.of(this).get(StartViewModel.class);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		AnnotationTool.init(this);
		viewModel.mode.observe(this, new Observer<Integer>(){

				@Override
				public void onChanged(Integer p1)
				{
					Tuke.write("mode",p1);
					new Handler().postDelayed(new Runnable() {
							@Override
							public void run()
							{
								startActivity(new Intent(StartActivity.this, PactActivity.class));
								StartActivity.this.finish();
							}
						}, 500);
				}
			});
    }
	
	public void chooseMode(View v)
	{
		//new ListPopupWindow();
		PopupMenu popup = new PopupMenu(this, v);
		popup.setGravity(Gravity.CENTER);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.choose_mode, popup.getMenu());
		popup.setOnMenuItemClickListener(this);
		popup.show();
	}

	@Override
	public boolean onMenuItemClick(MenuItem p1)
	{
		choose_mode.setText(p1.getTitle());
		viewModel.setMode(p1.getItemId());
		return false;
	}

}
