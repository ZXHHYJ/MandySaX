package mandysax.Lifecycle;
import java.util.*;
import mandysax.Lifecycle.LifecycleEvent.*;

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

	public void KillEvent(LifecycleViewModel p1)
	{
		if (ViewModel == null)ViewModel = new ArrayList<LifecycleViewModel>();
		ViewModel.add(p1);
	}

	public void onStart()
	{
		if (Start != null)
			for (LifecycleStart start: Start)
				start.onStart();
	}

	public void onResume()
	{
		if (Resume != null)
			for (LifecycleResume resume: Resume)
				resume.onResume();
	}

	public void onPause()
	{
		if (Pause != null)
			for (LifecyclePause pause: Pause)
				pause.onPause();
	}

	public void onStop()
	{
		if (Stop != null)
			for (LifecycleStop stop: Stop)
				stop.onStop();
	}

	public void onDestory()
	{
		if (Destory != null)
			for (LifecycleDestory destory: Destory)
				destory.onDestory();
	}

	public void onKill()
	{
		if (ViewModel != null)
			for (LifecycleViewModel viewModel: ViewModel)
				viewModel.onKill();
	}

}
