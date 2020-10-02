package mandysax.core.util;

interface XlistImpl<E extends Object>
{
	public E add(E... p1);

	public E add(int p1, E p2);

	public E get(int p1);

	public E get(int p1, E p2);

	public boolean remove(int p1);

	public boolean remove(E p1);

	public int size();
	
	public void clear();

	public boolean isEmpty();
	
}
