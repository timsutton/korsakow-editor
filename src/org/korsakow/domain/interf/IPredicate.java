package org.korsakow.domain.interf;

import java.util.List;

import org.dsrg.soenea.domain.interf.IDomainObject;

public interface IPredicate extends IDomainObject<Long>, IDynamicProperties
{
	String getPredicateType();
	void setPredicateType(String type);
	
	void setPredicates(List<IPredicate> predicates);
	List<IPredicate> getPredicates();
}
