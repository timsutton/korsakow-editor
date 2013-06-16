package org.korsakow.ide.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Util
{
	public static interface Predicate<T> {
		boolean execute(T t);
	}
	public static String join(Object[] arr)
	{
		return join(arr, ",");
	}
	public static String join(Object[] arr, String glue)
	{
		StringBuilder builder = new StringBuilder();
		for (Object o : arr) {
			builder.append(o.toString()).append(glue);
		}
		if (builder.length() > glue.length())
			builder.delete(builder.length() - glue.length(), builder.length());
		return builder.toString();
	}
	public static String join(Collection<?> coll)
	{
		return join(coll, ",");
	}
	public static String join(Collection<?> coll, String glue)
	{
		StringBuilder builder = new StringBuilder();
		for (Object o : coll) {
			builder.append(o.toString()).append(glue);
		}
		if (builder.length() > 0)
			builder.delete(builder.length() - glue.length(), builder.length());
		return builder.toString();
	}
	public static <O extends Object> O[] array(Class<O> clazz, O ...args)
	{
		O[] array = (O[])java.lang.reflect.Array.newInstance(clazz, args.length);
		System.arraycopy(args, 0, array, 0, args.length);
		return array;
	}
	public static <O extends Object> O[] arrayAdd(O[] src, O ...args)
	{
		O[] array = (O[])java.lang.reflect.Array.newInstance(src.getClass().getComponentType(), args.length + src.length);
		System.arraycopy(src, 0, array, 0, src.length);
		System.arraycopy(args, 0, array, src.length, args.length);
		return array;
	}
	public static <M> List<M> list(Class<M> clazz, M... args)
	{
		return asList(array(clazz, args));
	}
	public static <M> List<M> asList(M[] array)
	{
		List<M> list = new ArrayList<M>();
		for (M m : array)
			list.add(m);
		return list;
	}
	
	public static <T> List<T> filterList(Collection<T> src, Predicate<T> p) {
		List<T> list = new ArrayList<T>();
		for (T t : src) {
			if (p.execute(t))
				list.add(t);
		}
		return list;
	}
	
	/**
	 * Converts a collection of weak reference to a collection of "strong" references.
	 * 
	 * References that are empty/null are omitted.
	 * 
	 * @param <T>
	 * @param weakCollection
	 * @return
	 */
	public static <T> Collection<T> extractReferences(Collection<WeakReference<T>> weakCollection)
	{
		// we basically wrap the reflection related exceptions in a runtimeexception since we don't
		// realistically expect them to occur since collections typically have default constructors
		try {
			// instantiating and casting like this is possible becaus of type-erasure in generics
			// otherwise i guess there would have to be some related functionality in the Class api
			Collection<T> references = weakCollection.getClass().newInstance();
			for (WeakReference<T> ref : weakCollection)
			{
				T strongRef = ref.get();
				if (strongRef != null)
					references.add(strongRef);
			}
			return references;
		// IllegalArgumentException; let this through since we'd otherwise just wrap it in one
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Clears the collection of any weak references that are null/empty.
	 * @param collection TODO: this SHOULD be typed Collection<WeakReference<?>> but i was not able to pass in collections such as Set<WeakReference<Object>> for some reason?!
	 */
	public static void cleanEmptyReferences(Collection<?> collection)
	{
		Collection<WeakReference<?>> toRemove = new ArrayList<WeakReference<?>>();
		for (Object obj : collection)
		{
			if (obj instanceof WeakReference) // due to either generics sucking or my lame skillz, see doc comment for the parameter
			{
				WeakReference<?> ref = (WeakReference<?>)obj;
				Object t = ref.get();
				if (t == null)
					toRemove.add(ref);
			}
		}
		collection.removeAll(toRemove);
	}
	
	public static String getStackTraceString(Throwable t)
	{
		if (t == null)
			return "null";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		String trace = sw.toString();
		if (t.getCause() != null)
			trace += "\nCause:\n" + getStackTraceString(t.getCause());
		return trace;
	}
	/**
	 * Returns a unique representation of the exception's stack trace.
	 * The intent is to be able to identify exceptions based on where they occurred, while not being limited
	 * by the message or parameters having changed.
	 * 
	 * Note that the returned String might be somewhat large since the entire stack is walked and recursed for all causes.
	 * 
	 * @param t
	 * @return
	 */
	public static String getStackTraceUUID(Throwable t)
	{
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] trace = t.getStackTrace();
		for (StackTraceElement elm : trace)
		{
			// filename+line should be unique but sometimes we don't get them at all
			sb.append(elm.getFileName() + elm.getLineNumber() + elm.getClassName() + elm.getMethodName());
//			sb.append("\n");
		}
		if (t.getCause() != null)
			sb.append(getStackTraceUUID(t.getCause()));
		return sb.toString();
	}
	
	public static Throwable getRootCause(Throwable t) {
		Throwable c = t.getCause();
		while (c != null && c.getCause() != null)
			c = c.getCause();
		return c;
	}
}
