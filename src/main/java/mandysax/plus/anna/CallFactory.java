package mandysax.plus.anna;

import java.lang.reflect.Field;
import java.util.HashMap;
import mandysax.plus.anna.annotation.ARRAY;
import mandysax.plus.anna.annotation.BOOLEAN;
import mandysax.plus.anna.annotation.INT;
import mandysax.plus.anna.annotation.LONG;
import mandysax.plus.anna.annotation.PATH;
import mandysax.plus.anna.annotation.STRING;
import mandysax.utils.FieldUtils;
import mandysax.utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class CallFactory
{
	/*
	 *构建model实体类
	 */

	public static Object create(Class callClass, String content) throws IllegalAccessException, InstantiationException, JSONException
	{

		Object object = callClass.newInstance();
		if (content == null)	
			return object;
		HashMap<String,String> pathMap=new HashMap<String,String>();
		for (Field field:object.getClass().getDeclaredFields())
		{
			if (field.isAnnotationPresent(PATH.class))
			{
				PATH path=field.getAnnotation(PATH.class);
				pathMap.put(field.getName(), JsonUtils.Parsing(content, path.value().split("/")));
			}
		}
		for (Field field : object.getClass().getDeclaredFields())
		{
			String json=pathMap.get(field.getName()) != null ?pathMap.get(field.getName()): content;
			Object tokener = new JSONTokener(json).nextValue();
			if (field.isAnnotationPresent(BOOLEAN.class))
			{
				BOOLEAN _boolean=field.getAnnotation(BOOLEAN.class);
				FieldUtils.setField(field, object, tokener instanceof JSONObject ?new JSONObject(json).optBoolean(_boolean.value()): new JSONArray(json).getJSONObject(0).getBoolean(_boolean.value()));
			}
			else
			if (field.isAnnotationPresent(STRING.class))
			{
				STRING string = field.getAnnotation(STRING.class);
				FieldUtils.setField(field, object, tokener instanceof JSONObject ?new JSONObject(json).optString(string.value()): new JSONArray(json).getJSONObject(0).getString(string.value()));
			}
			else
			if (field.isAnnotationPresent(INT.class))
			{
				INT Int = field.getAnnotation(INT.class);
				FieldUtils.setField(field, object, tokener instanceof JSONObject ?new JSONObject(json).optInt(Int.value()): new JSONArray(json).getJSONObject(0).getInt(Int.value()));
			}
			else
			if (field.isAnnotationPresent(LONG.class))
			{
				LONG _long = field.getAnnotation(LONG.class);
				FieldUtils.setField(field, object, tokener instanceof JSONObject ?new JSONObject(json).optLong(_long.value()): new JSONArray(json).getJSONObject(0).getLong(_long.value()));
			}
			else
			if (field.isAnnotationPresent(ARRAY.class))
			{
				ARRAY array = field.getAnnotation(ARRAY.class);
				FieldUtils.setField(field, object, tokener instanceof JSONObject ?new JSONObject(json).optJSONArray(array.value()): new JSONArray(json).getJSONObject(0).getJSONArray(array.value()));
			}
		}
		return object;
	}
}
