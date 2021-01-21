package mandysax.plus.lifecycle;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public final class LifecycleContentProvider extends ContentProvider
{

	@Override
	public boolean onCreate()
	{
		AndroidViewModel.init(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri p1, String[] p2, String p3, String[] p4, String p5)
	{
		return null;
	}

	@Override
	public String getType(Uri p1)
	{
		return null;
	}

	@Override
	public Uri insert(Uri p1, ContentValues p2)
	{
		return null;
	}

	@Override
	public int delete(Uri p1, String p2, String[] p3)
	{
		return 0;
	}

	@Override
	public int update(Uri p1, ContentValues p2, String p3, String[] p4)
	{
		return 0;
	}
	
}
