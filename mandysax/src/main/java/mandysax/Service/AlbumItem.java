package mandysax.Service;
import java.io.Serializable;

public class AlbumItem implements Serializable
{
	private String title;
	private String url;
	private String cover;
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public void setUrl(String url){
		this.url=url;
	}
	
	public void setCover(String cover){
		this.cover=cover;
	}
	
	public String getTitle(){
		return title;
	}

	public String getUrl(){
		return url;
	}

	public String getCover(){
		return cover;
	}
	
}
