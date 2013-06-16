package test.org.korsakow.service.plugin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestPredicatePluginArguments.class,
	TestPredicateTypeCoverage.class,
	TestRulePluginArguments.class,
	TestRuleTypeCoverage.class,
})
@RunWith(Suite.class)
public class PluginSuite {
}
