package test.org.korsakow.domain;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestDataRegistry.class,
	TestDomSession.class,
	TestDomainObjectEquality.class,
	TestDomainObjectClone.class,
	TestMappingCoherence.class,
	TestMapperHelperProxy.class,
	TestMapperHelperConcrete.class,
	TestMapperHelperFindResourcesReferencing.class,
})
@RunWith(Suite.class)
public class DomainSuite {
}
