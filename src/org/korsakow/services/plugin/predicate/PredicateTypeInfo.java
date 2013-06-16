/**
 * 
 */
package org.korsakow.services.plugin.predicate;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PredicateTypeInfo implements IPredicateTypeInfo
{
	public static PredicateTypeInfo create(String id, int arity, String displayString, String formattedDisplayString, IArgumentInfo... args)
	{
		return new PredicateTypeInfo(id, arity, displayString, formattedDisplayString, args);
	}
	public static PredicateTypeInfo create(String id, int arity, String displayString)
	{
		return new PredicateTypeInfo(id, arity, displayString, displayString, new IArgumentInfo[0]);
	}
	private final String id;
	private final String displayString;
	private final String formattedDisplayString;
	private final int arity;
	private final List<IArgumentInfo> arguments;
	private final Map<String, IArgumentInfo> argmap;
	public PredicateTypeInfo(String id, int arity, String displayString, String formattedDisplayString, IArgumentInfo... args)
	{
		this.id = id;
		this.displayString = displayString;
		this.formattedDisplayString = formattedDisplayString;
		this.arity = arity;
		this.arguments = Arrays.asList(args);
		this.argmap = new HashMap<String, IArgumentInfo>();
		for (IArgumentInfo info : args)
			argmap.put(info.getName(), info);
	}
	public String getId() { return id; }
	public String getDisplayString() { return displayString; }
	public String getFormattedDisplayString(Object... args)
	{
		if (args.length != arguments.size())
			throw new IllegalArgumentException("Argument count mismatch; expecting " + arguments.size() + ", got " + args.length);
		String[] fargs = new String[args.length];
		for (int i = 0; i < args.length; ++i)
			fargs[i] = arguments.get(i).getFormattedDisplayString(args[i]);
		return MessageFormat.format(formattedDisplayString, fargs);
	}
	public int getArity() { return arity; }
	public Collection<IArgumentInfo> getArguments() { return arguments; }
	public IArgumentInfo getArgument(String name) { return argmap.get(name); }
}
