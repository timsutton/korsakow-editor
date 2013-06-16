package org.korsakow.domain.interf;

import java.util.Collection;
import java.util.List;

public interface IRule extends IResource, IDynamicProperties
{
	String getRuleType();
	void setRuleType(String type);
	
	Collection<IKeyword> getKeywords();
	void setKeywords(Collection<IKeyword> keywords);
	
	long getTriggerTime();
	void setTriggerTime(long triggerTime);
	
	void setRules(List<IRule> rules);
	List<IRule> getRules();
}
