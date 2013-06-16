package test.org.korsakow.service.plugin;

import org.dsrg.soenea.uow.UoW;
import org.junit.Assert;
import org.junit.Test;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.ide.Application;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.predicate.IPredicateTypeInfo;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;

import test.util.DomainTestUtil;

public class TestPredicatePluginArguments extends AbstractPluginTest {
	@Test public void testKeywordInHistory() throws Exception
	{
		IPredicateTypeInfo typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(PredicateType.KeywordInHistory.getId());
		Object testValue = KeywordFactory.createClean(DomainTestUtil.getRandomString());
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		Assert.assertTrue(argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
	@Test public void testSnuInHistory() throws Exception
	{
		UoW.newCurrent();
		IPredicateTypeInfo typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(PredicateType.SnuInHistory.getId());
		ISnu snu = SnuFactory.createNew( DomainTestUtil.getRandomLong(), 0 );
		UoW.getCurrent().registerNew( snu );
		UoW.getCurrent().commit();
		Object testValue = snu;
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		Assert.assertTrue(String.format(argInfo.getType() + " Assignable From " + testValue.getClass()), argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
	@Test public void testPercentOfSnusInHistory() throws Exception
	{
		IPredicateTypeInfo typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(PredicateType.PercentOfSnusInHistory.getId());
		Object testValue = DomainTestUtil.getRandomDouble();
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		Assert.assertTrue(argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
	@Test public void testNumberOfSnusInHistory() throws Exception
	{
		IPredicateTypeInfo typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(PredicateType.NumberOfSnusInHistory.getId());
		Object testValue = DomainTestUtil.getRandomLong();
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		Assert.assertTrue(argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
}
