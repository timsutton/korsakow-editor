package test.org.korsakow;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	test.org.korsakow.action.ActionSuite.class,
	test.org.korsakow.dom.DomSuite.class,
	test.org.korsakow.domain.DomainSuite.class,
	test.org.korsakow.command.CommandSuite.class,
	test.org.korsakow.export.ExportSuite.class,
	test.org.korsakow.k3.K3Suite.class,
	test.org.korsakow.media.MediaSuite.class,
	test.org.korsakow.service.ServiceSuite.class,
	test.org.korsakow.ui.UISuite.class,
	test.org.korsakow.util.UtilSuite.class,
//suite.addTest(test.org.korsakow.rule.AllTests.suite()); // these are kind of annoying so do them near the end
//suite.addTest(test.org.korsakow.encoding.AllTests.suite()); // these are long... do 'em last
})
@RunWith(Suite.class)
public class KorsakowSuite {
}
