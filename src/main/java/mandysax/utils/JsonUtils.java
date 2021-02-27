package mandysax.utils;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils
{
	public static String Parsing(String content, String... key)
	{
		if(key==null)return content;
		for (String string: key)
		{
			try
			{
				content = new JSONObject(content).optString(string);
			}
			catch (JSONException e)
			{
			}
		}
		return content;
	}
	
}
