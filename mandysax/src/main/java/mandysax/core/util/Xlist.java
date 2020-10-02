package mandysax.core.util;

import java.util.Arrays;
import mandysax.data.SafetyHashMap;

public class Xlist<E extends Object> implements XlistImpl
{
	private int[] key=new int[0];

	private E old_data;

	private final SafetyHashMap<Integer,E> map=new SafetyHashMap<Integer,E>();

	@Override
	public E add(E... p1)
	{
		for (E content:p1)
		{
			return add(size() + 1, content);
		}
		return null;
	}

	@Override
	public E add(int p1, E p2)
	{
		if (p2 == null)throw new NullPointerException("Cannot add null");
		if (p1 > key.length)
		{
			int[] copy = Arrays.copyOf(key, p1);
			for (int i=key.length;i < p1;i++)
			{
				copy[i] = p2.hashCode();
				map.put(copy[i], p2);
			}
			key = copy;
		}
		else
		{
			int b[] = new int[key.length];
			for (int i=0;i < b.length;i++)
				if (i == p1 - 1)
				{
					b[p1 - 1] = p2.hashCode();
					if (old_data == null)
						map.put(b[p1 - 1], p2);
					else
					if (old_data.hashCode() != p2)
						map.put(b[p1 - 1], p2);
				}
				else if (key != null)
				{
					b[i] = key[i];
				}
			old_data = p2;
			key = b;
		}
		return p2;
	}

	@Override
	public E get(int p1)
	{
		if (key != null)
			if (p1 >= key.length)throw new ArrayIndexOutOfBoundsException("Array out of bounds. " + "index:" + p1 + " lenght:" + key.length);
			else
				return map.get(key[p1]);
		return null;
	}

	@Override
	public E get(int p1, E p2)
	{
		final E _return=get(p1);
		if (_return == null)
			return p2;
		else return _return;
	}

	@Override
	public boolean remove(int p1)
	{
		return remove(map.get(key[p1]));
	}

	@Override
	public boolean remove(E p1)
	{
		if (p1 == null)return false;     
		int len = key.length;
        for (int i = 0; i < key.length; i++)
		{
            if (key[i] == p1.hashCode())
			{
                System.arraycopy(key, i + 1, key, i, len - 1 - i);
                break;
            }
        }
		key = Arrays.copyOf(key, len - 1);
		map.remove(p1.hashCode());
		return map.get(p1.hashCode()) == null;
	}

	@Override
	public int size()
	{
		if (key != null)
			return key.length;
		return 0;
	}

	@Override
	public void clear()
	{
		key = null;
		map.clear();
	}

	@Override
	public boolean isEmpty()
	{
		return size() == 0;
	}

}

