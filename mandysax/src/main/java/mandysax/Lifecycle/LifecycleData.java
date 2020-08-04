package mandysax.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class LifecycleData
{	
	protected final List<Lifecycle> lifecycle = new ArrayList<Lifecycle>();

	protected void add(Lifecycle p1)
	{
		lifecycle.add(p1);
	}

	protected void remove(Object p1)
	{
		lifecycle.remove(p1);
	}
	
	protected <T> Lifecycle contains(T obj){
		if(lifecycle.contains(obj)){
			return lifecycle.get(lifecycle.indexOf(obj));
		}	
		return null;
	}

}
