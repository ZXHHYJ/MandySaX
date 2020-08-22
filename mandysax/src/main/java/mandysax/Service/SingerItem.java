package mandysax.Service;
import java.io.Serializable;

public class SingerItem implements Serializable
{
	
	private String name;
	
	private int id;
	
	public void setId(int id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
}
