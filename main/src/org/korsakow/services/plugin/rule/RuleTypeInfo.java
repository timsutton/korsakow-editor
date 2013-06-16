/**
 * 
 */
package org.korsakow.services.plugin.rule;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.services.plugin.predicate.IArgumentInfo;


public class RuleTypeInfo implements IRuleTypeInfo
{
	public static RuleTypeInfo create(String id, String displayString, String formattedDisplayString, IArgumentInfo... args)
	{
		return new RuleTypeInfo(id, displayString, formattedDisplayString, args);
	}
	public static RuleTypeInfo create(String id, String displayString)
	{
		return new RuleTypeInfo(id, displayString, displayString, new IArgumentInfo[0]);
	}
	private final String id;
	private final String displayString;
	private final String formattedDisplayString;
	private final List<IArgumentInfo> arguments;
	private final Map<String, IArgumentInfo> argmap;
	public RuleTypeInfo(String id, String displayString, String formattedDisplayString, IArgumentInfo... args)
	{
		this.id = id;
		this.displayString = displayString;
		this.formattedDisplayString = formattedDisplayString;
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
		Object[] fargs = new String[args.length];
		for (int i = 0; i < args.length; ++i)
			fargs[i] = arguments.get(i).getFormattedDisplayString(args[i]);
		// the call to escape is a hack because our language bundles aren't properly escaped
		return MessageFormat.format(LanguageBundle.escapeMessageFormat(formattedDisplayString), fargs);
	}
	public Collection<IArgumentInfo> getArguments() { return arguments; }
	public IArgumentInfo getArgument(String name) { return argmap.get(name); }
}
