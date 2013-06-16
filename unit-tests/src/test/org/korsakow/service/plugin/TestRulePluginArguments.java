package test.org.korsakow.service.plugin;

import org.dsrg.soenea.uow.UoW;
import org.junit.Assert;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.Application;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.rule.IRuleTypeInfo;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactory;

import test.util.DomainTestUtil;

public class TestRulePluginArguments extends AbstractPluginTest {
	@Test public void testPlayMedia() throws Exception
	{
		UoW.newCurrent();
		IRuleTypeInfo typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(RuleType.PlayMedia.getId());
		IMedia media = ImageFactory.createNew( DomainTestUtil.getRandomLong(), 0 );
		UoW.getCurrent().registerNew( media );
		UoW.getCurrent().commit();
		Object testValue = media;
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		argInfo.getDisplayString();
		argInfo.getFormattedDisplayString(testValue);
		Assert.assertTrue(argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
	@Test public void testSetSnuLives() throws Exception
	{
		IRuleTypeInfo typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(RuleType.SetSnuLives.getId());
		Object testValue = DomainTestUtil.getRandomLong();
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		Assert.assertTrue(argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
	@Test public void testSearch() throws Exception
	{
		IRuleTypeInfo typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(RuleType.Search.getId());
		Object testValue = DomainTestUtil.getRandomLong();
		IArgumentInfo argInfo = typeInfo.getArguments().iterator().next();
		Assert.assertTrue(argInfo.getType().isAssignableFrom(testValue.getClass()));
		Assert.assertEquals(testValue, argInfo.deserialize(argInfo.serialize(testValue)));
	}
}
