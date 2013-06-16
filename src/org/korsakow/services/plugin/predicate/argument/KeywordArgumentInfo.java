/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;

import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.interf.IKeyword;

public class KeywordArgumentInfo extends AbstractArgumentInfo
{
	public KeywordArgumentInfo(String name, String displayString)
	{
		super(name, IKeyword.class, displayString);
	}
	public String formatDisplayValue(Object value)
	{
		return ((IKeyword)value).getValue();
	}
	public Object deserialize(String value) {
		return KeywordFactory.createClean(value);
	}
	public String serialize(Object value) {
		return ((IKeyword)value).getValue();
	}
}