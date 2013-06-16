package org.korsakow.domain.proxy;

import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Predicate;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.mapper.input.PredicateInputMapper;

public class PredicateProxy extends KDomainObjectProxy<Predicate> implements IPredicate {

	public PredicateProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Predicate> getInnerClass()
	{
		return Predicate.class;
	}
	
	@Override
	protected Predicate getFromMapper(Long id) throws MapperException {
		return PredicateInputMapper.map(id);
	}

	public String getPredicateType() {
		return getInnerObject().getPredicateType();
	}

	public void setPredicateType(String predicateType) {
		getInnerObject().setPredicateType(predicateType);
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
	public void setPredicates(List<IPredicate> predicates)
	{
		getInnerObject().setPredicates(predicates);
	}
	public List<IPredicate> getPredicates()
	{
		return getInnerObject().getPredicates();
	}
}
