package mandysax.Lifecycle.Paradrop;
import mandysax.Lifecycle.LifecycleAbstract;
import mandysax.Lifecycle.LifecycleEvent.LifecycleViewModel;

public class paradrop
{
	public static <T> T newTask(String key, T obj)
	{	
		paradropManagement.paradropManagement.add(new paradropItem(key, obj));
		return obj;
	}

	public static <T> T newTask(LifecycleAbstract lifecycle, String key, T obj)
	{	
		final paradropItem context = new paradropItem(key, obj);
	    paradropManagement.paradropManagement.add(context);
		lifecycle.getLifecycle().KillEvent(new LifecycleViewModel(){

				@Override
				public void onKill()
				{
					paradropManagement.paradropManagement.remove(context);
				}
			});
		return obj;
	}
	
	public static <T> T getDrop(String key){
		for(int i = 0;i<paradropManagement.paradropManagement.size();i++){
			if(paradropManagement.paradropManagement.get(i).getKey().equals(key)){
				return (T)paradropManagement.paradropManagement.get(i).getObj();
			}
		}
		return null;
	}
}
