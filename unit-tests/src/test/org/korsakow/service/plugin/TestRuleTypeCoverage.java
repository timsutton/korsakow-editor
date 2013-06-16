package test.org.korsakow.service.plugin;

import java.io.IOException;

import org.junit.Test;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactory;

public class TestRuleTypeCoverage extends AbstractPluginTest {
	@Test public void testAllTypesRegisteredInFactory() throws IOException
	{
		RuleTypeInfoFactory fac = RuleTypeInfoFactory.getFactory();
		for (RuleType type : RuleType.values())
			fac.getTypeInfo(type.getId());
	}
}
