package mandysax.anna2.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtils
{
	public static String Parsing(String content, String... key) throws JSONException {
		if (key == null)return content;
		for (String name: key)
		{
			content = new JSONTokener(content).nextValue() instanceof JSONObject ?new JSONObject(content).optString(name): new JSONArray(content).optJSONObject(0).optString(name);

		}
		return content;
	}

}
