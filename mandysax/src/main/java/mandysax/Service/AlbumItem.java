package mandysax.Service;
import java.io.Serializable;

public class AlbumItem implements Serializable
{
	private String title;
	private String url;
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public void setUrl(String url){
		this.url=url;
	}
	
	public String getTitle(){
		return title;
	}

	public String getUrl(){
		return url;
	}
	
}
