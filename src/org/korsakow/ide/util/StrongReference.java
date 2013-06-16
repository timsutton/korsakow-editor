package org.korsakow.ide.util;

public class StrongReference<T>
{
	private T ref;
	public StrongReference()
	{
		this(null);
	}
	public StrongReference(T ref)
	{
		set(ref);
	}
	public boolean isNull()
	{
		return ref == null;
	}
	public T get()
	{
		return ref;
	}
	public void set(T ref)
	{
		this.ref = ref;
	}
	public void clear()
	{
		ref = null;
	}
}
