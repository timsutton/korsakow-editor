package test.org.korsakow.service.plugin;

import java.io.IOException;

import org.junit.Test;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;

public class TestPredicateTypeCoverage extends AbstractPluginTest {
	@Test public void testAllTypesRegisteredInFactory() throws IOException
	{
		PredicateTypeInfoFactory fac = PredicateTypeInfoFactory.getFactory();
		for (PredicateType type : PredicateType.values())
			fac.getTypeInfo(type.getId());
	}
}
