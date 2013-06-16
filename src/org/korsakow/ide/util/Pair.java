package org.korsakow.ide.util;

public class Pair<A, B>
{
	private A a;
	private B b;
	public Pair(A a, B b)
	{
		this.a = a;
		this.b = b;
	}
	public Pair()
	{
		this(null, null);
	}
	public A getFirst()
	{
		return a;
	}
	public B getSecond()
	{
		return b;
	}
}
