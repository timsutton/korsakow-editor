package test.org.korsakow.dom;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestDomUtilPath.class,
	TestDomUtilValue.class,
})
@RunWith(Suite.class)
public class DomSuite {
}
