package test.org;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	test.org.korsakow.KorsakowSuite.class,
})
@RunWith(Suite.class)
public class OrgSuite {
}
