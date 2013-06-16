package org.korsakow.domain.proxy;

import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Rule;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.mapper.input.RuleInputMapper;

public class RuleProxy extends ResourceProxy<Rule> implements IRule {

	public RuleProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Rule> getInnerClass()
	{
		return Rule.class;
	}
	
	@Override
	protected Rule getFromMapper(Long id) throws MapperException {
		return RuleInputMapper.map(id);
	}

	public String getRuleType() {
		return getInnerObject().getRuleType();
	}

	public void setRuleType(String type) {
		getInnerObject().setRuleType(type);
	}

	@Override
	public Collection<IKeyword> getKeywords() {
		return getInnerObject().getKeywords();
	}

	@Override
	public void setKeywords(Collection<IKeyword> keywords) {
		getInnerObject().setKeywords(keywords);
	}

	@Override
	public long getVersion() {
		return getInnerObject().getVersion();
	}

	@Override
	public void setVersion(long new_version) {
		getInnerObject().setVersion(new_version);
	}

	public long getTriggerTime()
	{
		return getInnerObject().getTriggerTime();
	}
	public void setTriggerTime(long triggerTime)
	{
		getInnerObject().setTriggerTime(triggerTime);
	}
	public Object getDynamicProperty(String id) {
		return getInnerObject().getDynamicProperty(id);
	}

	public Collection<String> getDynamicPropertyIds() {
		return getInnerObject().getDynamicPropertyIds();
	}

	public void setDynamicProperty(String id, Object value) {
		getInnerObject().setDynamicProperty(id, value);
	}
	public Class getPropertyType(String id)
	{
		return getInnerObject().getPropertyType(id);
	}
	public void setRules(List<IRule> rules)
	{
		getInnerObject().setRules(rules);
	}
	public List<IRule> getRules()
	{
		return getInnerObject().getRules();
	}
}
