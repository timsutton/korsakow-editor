package org.korsakow.ide.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * @deprecated use StringTemplate instead
 * @author d
 *
 */
public class Template
{
	private String template;
	private Map<String, String> replacements = new Hashtable<String, String>();
	
	public Template(String template)
	{
		this.template = template;
	}
	public void setValue(String name, Object value)
	{
		replacements.put(name, value.toString());
	}
	public void setValues(Map<String, String> replacements)
	{
		this.replacements.putAll(replacements);
	}
	public void clearValues()
	{
		replacements.clear();
	}
	public boolean isComplete(String source)
	{
		return source.indexOf("${") == -1; // shoddy implementation
	}
	public String format()
	{
		// todo: a sane implementation
		String s = template;
		for (String name : replacements.keySet())
		{
			String value = replacements.get(name);
			s = s.replace("${" + name + "}", value);
		}
		if (!isComplete(s))
			throw new IllegalArgumentException("template incomplete: " + s);
		return s;
	}
}
