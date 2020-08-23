package mandysax.Lifecycle.Anna;

import android.graphics.*;
import java.io.*;
import java.net.*;
import java.util.*;
import mandysax.Data.*;

class AnnaTask
{

	public static String inputStream2String(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1)
        {
            byteArrayOutputStream.write(i);
        }
        return byteArrayOutputStream.toString();
    }

	public static Bitmap getBitmap(URL url) throws IOException
	{
		URLConnection conn = url.openConnection();
		conn.connect();
		InputStream in = conn.getInputStream();
		return BitmapFactory.decodeStream(in);
	}

	public static String getString(URL url) throws ProtocolException, IOException
	{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");   
		int responseCode = connection.getResponseCode();  
		if (responseCode == 200)
		{           
			InputStream is = connection.getInputStream();  
			return inputStream2String(is);
		}
		else return null;
	}

	public static String Parsing(List<String> list_string, String content)
	{
	    String _content=content;
		for (String content2:list_string)
		{
			_content = get.jsonstring(_content, content2);
		}
		return _content;
	}

}
