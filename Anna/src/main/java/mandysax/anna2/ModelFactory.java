package mandysax.anna2;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import mandysax.anna2.annotation.Path;
import mandysax.anna2.annotation.Value;
import mandysax.anna2.utils.FieldUtils;
import mandysax.anna2.utils.GenericUtils;
import mandysax.anna2.utils.JsonUtils;

public final class ModelFactory {
    /*
     *模型工厂
     */

    public static <T> T create(Class<T> modelClass, String content) throws IllegalAccessException, InstantiationException, JSONException {
        T object = modelClass.newInstance();
        if (content == null)
            return object;
        HashMap<String, String> pathMap = new HashMap<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Path.class)) {
                Path path = field.getAnnotation(Path.class);
                assert path != null;
                pathMap.put(field.getName(), JsonUtils.Parsing(content, path.value().split("/")));
            }
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getAnnotations().length != 0) {
                String json = pathMap.get(field.getName()) != null ? pathMap.get(field.getName()) : content;
                String name = Objects.requireNonNull(field.getAnnotation(Value.class)).value();
                if (field.getType() == Boolean.class) {
                    FieldUtils.setField(field, object, parsingBoolean(name, json));
                } else if (field.getType() == String.class) {
                    FieldUtils.setField(field, object, parsingString(name, json));
                } else if (field.getType() == int.class) {
                    FieldUtils.setField(field, object, parsingInt(name, json));
                } else if (field.getType() == long.class) {
                    FieldUtils.setField(field, object, parsingLong(name, json));
                } else if (field.getType() == List.class || field.getType() == ArrayList.class) {
                    JSONArray array = parsingJSONArray(name, json);
                    Class<?> classType = GenericUtils.getGenericType(field);
                    ArrayList list = new ArrayList();
                    if (classType != null) {
                        if (array != null)
                            for (int i = 0; i < array.length(); i++) {
                                list.add(ModelFactory.create(classType, array.getString(i)));
                            }
                    }
                    FieldUtils.setField(field, object, list);
                } else FieldUtils.setField(field, object, parsingObject(field, json));
            }
        }
        return object;
    }

    static JSONObject getNextValue(String in) throws JSONException {
        return new JSONTokener(in).nextValue() instanceof JSONObject ? new JSONObject(in) : new JSONArray(in).optJSONObject(0);
    }

    static boolean parsingBoolean(String name, String json) throws JSONException {
        return getNextValue(json).optBoolean(name);
    }

    static int parsingInt(String name, String json) throws JSONException {
        return getNextValue(json).optInt(name);
    }

    static long parsingLong(String name, String json) throws JSONException {
        return getNextValue(json).optLong(name);
    }

    @NonNull
    static String parsingString(String name, String json) throws JSONException {
        return getNextValue(json).optString(name);
    }

    @NonNull
    static Object parsingObject(@NonNull Field field, String json) throws JSONException, IllegalAccessException, InstantiationException {
        return ModelFactory.create(field.getType(), json);
    }

    static JSONArray parsingJSONArray(String name, String json) throws JSONException {
        return getNextValue(json).optJSONArray(name);
    }

}
