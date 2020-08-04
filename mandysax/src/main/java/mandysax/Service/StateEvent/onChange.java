package mandysax.Service.StateEvent;
import mandysax.Service.MediaService;

public abstract interface onChange
{
    public abstract void onEvent(MediaService.MusicBinder root,int mode);
}
