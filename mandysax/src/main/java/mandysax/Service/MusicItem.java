package mandysax.Service;
import java.io.Serializable;
import java.util.List;
import android.graphics.*;
import mandysax.Lifecycle.LiveData.*;

public class MusicItem implements Serializable
{
    private String title;
	private String album_title;
	private MutableLiveData<Bitmap> _album = new MutableLiveData<Bitmap>();
    public LiveData<Bitmap> album =_album; 
	private List<SingerItem> singer;
    private int id;
	
	public void setTitle(String title){
		this.title=title;
	}
	
	public void setSinger(List<SingerItem> singer){
		this.singer=singer;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getTitle(){
		return title;
	}
	
	public List<SingerItem> getSinger(){
		return singer;
	}
	
	public void setAlbumTitle(String title){
		this.album_title=title;
	}

	public String getAlbumTitle(){
		return album_title;
	}
	
	public void setAlbum(Bitmap album){
		this._album.setValue(album);
	}
	
	public int getId(){
		return id;
	}
	
}
