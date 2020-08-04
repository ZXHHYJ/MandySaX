package com.FMJJ.MandySa;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.FMJJ.MandySa.R;
import com.FMJJ.MandySa.ViewModel.StartViewModel;
import mandysax.Lifecycle.LifecycleActivity;
import mandysax.Lifecycle.LiveData.Observer;
import mandysax.Lifecycle.ViewModel.ViewModelProviders;
import simon.tuke.Tuke;

public class StartActivity extends LifecycleActivity implements PopupMenu.OnMenuItemClickListener
{
	private TextView choose_mode;

	private final StartViewModel viewModel= ViewModelProviders.of(this).get(StartViewModel.class);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
		choose_mode=findViewById(R.id.start_choose_mode);
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
