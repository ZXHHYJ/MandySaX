package com.FMJJ.MandySa.logic;
import mandysax.data.repository.Repository;
import mandysax.data.repository.DataCallback;
import mandysax.data.repository.Key;
import com.FMJJ.MandySa.logic.model.MusicItem;
import com.FMJJ.MandySa.logic.dao.LikeMusicDao;
import android.graphics.Bitmap;

public class CoverRepository extends mandysax.data.repository.Repository<Bitmap>
{
    public final static String GET_COVER_KEY="name";
    
    @Override
    public void getLocalData(Key key, DataCallback<Bitmap> callBack)
    {
        if(key==null)return;
        key.getString(GET_COVER_KEY);
    }

}
