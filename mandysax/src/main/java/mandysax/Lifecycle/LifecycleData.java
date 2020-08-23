package mandysax.Lifecycle;
import java.util.*;

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

}
