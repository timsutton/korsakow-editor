package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;



@SuiteClasses({
	test.bugs.BugsSuite.class,
	test.org.OrgSuite.class
})
@RunWith(Suite.class)
public class RootSuite {

}
