package mandysax.anna2.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/*2021.5.19*/
public final class GenericUtils
{

	public static Class getGenericType(Method method)
	{
		Type type=method.getGenericReturnType();
		if (type instanceof ParameterizedType)
		{
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type[] types = parameterizedType.getActualTypeArguments();
			for (Type value : types) {
				Class cls = (Class) value;
				return cls;
			}
		}
		return null;
	}

	public static Class getGenericType(Field field)
	{
		Type genericsFieldType=field.getGenericType();   
		if (genericsFieldType instanceof ParameterizedType)
		{      
			ParameterizedType parameterizedType=(ParameterizedType) genericsFieldType;     
			Type[] fieldArgTypes=parameterizedType.getActualTypeArguments();  
			for (Type fieldArgType:fieldArgTypes)
			{            
				Class fieldArgClass=(Class) fieldArgType;   
				return fieldArgClass;	
			}       
		}
		return null;
	}

}
