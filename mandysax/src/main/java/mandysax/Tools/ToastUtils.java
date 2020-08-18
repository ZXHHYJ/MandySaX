package mandysax.Tools;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils
{
    private static Context con;

    public static void init(Context context)
	{
        con = context;
    }
    public static void showToast(CharSequence text)
	{
        if (text != null && con != null)Toast.makeText(con, text, 10).show();
    }

}
