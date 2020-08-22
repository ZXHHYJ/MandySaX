package mandysax.Lifecycle.Anna.AnnaEvent;
import java.io.*;
import android.graphics.*;

public interface onEvent<T>
{
	public void onEnd(T decodeStream);
	public void onError()
}
