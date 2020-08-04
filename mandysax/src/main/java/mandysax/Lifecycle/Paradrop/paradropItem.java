package mandysax.Lifecycle.Paradrop;

public class paradropItem
{
	private final String key;
	private final Object obj;
	
	public paradropItem(String key,Object obj){
		this.key=key;
		this.obj=obj;
	}
	
	public String getKey(){
		return key;
	}
	
	public Object getObj(){
		return obj;
	}
	
}
