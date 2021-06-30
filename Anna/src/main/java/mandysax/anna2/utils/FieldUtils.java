package mandysax.anna2.utils;

import java.lang.reflect.Field;

public final class FieldUtils
{
	public static void setField(Field field, Object main, Object content)
	{
		boolean lock=field.isAccessible();
		if (!lock)
			field.setAccessible(true);
		try
		{
			field.set(main, content);
			if (lock != field.isAccessible())
				field.setAccessible(lock);
		}
		catch (Exception ignored)
		{}
	}
}
