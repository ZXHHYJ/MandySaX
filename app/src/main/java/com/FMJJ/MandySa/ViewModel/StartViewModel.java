package com.FMJJ.MandySa.ViewModel;

import mandysax.Lifecycle.LiveData.LiveData;
import mandysax.Lifecycle.LiveData.MutableLiveData;
import mandysax.Lifecycle.ViewModel.ViewModel;

public class StartViewModel extends ViewModel
{
	private final MutableLiveData<Integer> _mode = new MutableLiveData<Integer>(0);

	public final LiveData <Integer> mode =  _mode;

	public void setMode(int p1)
	{
		_mode.setValue(p1);
	}
	
}
