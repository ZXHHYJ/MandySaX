package com.FMJJ.MandySa.ui;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.FMJJ.MandySa.R;
import mandysax.core.annotation.BindLayoutId;
import mandysax.core.annotation.BindView;
import mandysax.lifecycle.FragmentCompat;
import android.widget.Toast;

@BindLayoutId(R.layout.live_fragment)
public class LiveFragment extends FragmentCompat
{

	@BindView(R.id.recommendfragmentButton1)
	private Button button;
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		button.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					startFragment(SettingFragment.class);
				}	

			});
	}

}
