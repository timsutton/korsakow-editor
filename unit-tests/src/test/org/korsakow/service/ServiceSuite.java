package test.org.korsakow.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.org.korsakow.service.conversion.ConversionSuite;

@SuiteClasses({
	test.org.korsakow.service.tdg.TDGSuite.class,
	test.org.korsakow.service.plugin.PluginSuite.class,
	test.org.korsakow.service.updater.UpdateSuite.class,
	ConversionSuite.class,
})
@RunWith(Suite.class)
public class ServiceSuite {
}
