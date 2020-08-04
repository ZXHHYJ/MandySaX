package mandysax.Lifecycle;
import java.util.ArrayList;
import java.util.List;
import mandysax.Lifecycle.LifecycleEvent.LifecycleDestory;
import mandysax.Lifecycle.LifecycleEvent.LifecyclePause;
import mandysax.Lifecycle.LifecycleEvent.LifecycleResume;
import mandysax.Lifecycle.LifecycleEvent.LifecycleStart;
import mandysax.Lifecycle.LifecycleEvent.LifecycleStop;
import mandysax.Lifecycle.LifecycleEvent.LifecycleViewModel;

public class Lifecycle
{
	private List<LifecycleStart> Start;

	private List<LifecyclePause> Pause;

	private List<LifecycleStop> Stop;

	private List<LifecycleDestory> Destory;

	private List<LifecycleResume> Resume;
	
	private List<LifecycleViewModel> ViewModel;
	
	public void StartEvent(LifecycleStart p1)
	{
		if (Start == null)Start = new ArrayList<LifecycleStart>();
		Start.add(p1);
	}

	public void ResumeEvent(LifecycleResume p1)
	{
		if (Resume == null)Resume = new ArrayList<LifecycleResume>();
		Resume.add(p1);
	}

	public void PauseEvent(LifecyclePause p1)
	{
		if (Pause == null)Pause = new ArrayList<LifecyclePause>();
		Pause.add(p1);
	}

	public void StopEvent(LifecycleStop p1)
	{
		if (Stop == null)Stop = new ArrayList<LifecycleStop>();
		Stop.add(p1);
	}

	public void DestoryEvent(LifecycleDestory p1)
	{
		if (Destory == null)Destory = new ArrayList<LifecycleDestory>();
		Destory.add(p1);
	}
	
	public void KillEvent(LifecycleViewModel p1){
		if (ViewModel == null)ViewModel = new ArrayList<LifecycleViewModel>();
		ViewModel.add(p1);
	}

	public void onStart()
	{
		if (Start != null)
			for (int i = 0;i < Start.size();i++)
				Start.get(i).onStart();
	}

	public void onResume()
	{
		if (Resume != null)
			for (int i = 0;i < Resume.size();i++)
				Resume.get(i).onResume();
	}

	public void onPause()
	{
		if (Pause != null)
			for (int i = 0;i < Pause.size();i++)
				Pause.get(i).onPause();
	}

	public void onStop()
	{
		if (Stop != null)
			for (int i = 0;i < Stop.size();i++)
				Stop.get(i).onStop();
	}

	public void onDestory()
	{
		if (Destory != null)
			for (int i = 0;i < Destory.size();i++)
				Destory.get(i).onDestory();
	}
	
	public void onKill(){
		if (ViewModel != null)
			for (int i = 0;i < ViewModel.size();i++)
				ViewModel.get(i).onKill();
	}

}
