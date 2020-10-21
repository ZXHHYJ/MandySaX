package com.FMJJ.MandySa.ui.main;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.FMJJ.MandySa.R;
import mandysax.lifecycle.Fragment;

public class SettingFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container)
	{
		return inflater.inflate(R.layout.setting_fragment,container,false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
}
