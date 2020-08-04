package mandysax.Service;
import java.io.Serializable;
import java.util.List;

public class MusicItem implements Serializable
{
    private String title;
    private List<SingerItem> singer;
    private AlbumItem album;
    private String url;
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public void setSinger(List<SingerItem> singer){
		this.singer=singer;
	}
	
	public void setAlbum(AlbumItem album){
		this.album=album;
	}
	
	public void setUrl(String url){
		this.url=url;
	}
	
	public String getTitle(){
		return title;
	}
	
	public List<SingerItem> getSinger(){
		return singer;
	}
	
	public AlbumItem getAlbum(){
		return album;
	}
	
	public String getUrl(){
		return url;
	}
	
}
