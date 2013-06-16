package org.korsakow.domain;

import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.ide.DataRegistry;

public class PredicateFactory {
	public static Predicate createNew(String predicateType, List<IPredicate> predicates)
	{
		Predicate object = new Predicate(DataRegistry.getMaxId(), 0, predicateType, predicates);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Predicate createClean(String predicateType)
	{
		Predicate object = new Predicate(DataRegistry.getMaxId(), 0, predicateType, new ArrayList<IPredicate>());
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Predicate createClean(long id, long version, String predicateType, List<IPredicate> predicates)
	{
		Predicate object = new Predicate(id, version, predicateType, predicates);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Predicate copy(IPredicate src)
	{
		Predicate object = new Predicate(DataRegistry.getMaxId(), 0, src.getPredicateType(), src.getPredicates());
		UoW.getCurrent().registerNew(object);
		return object;
	}
}
