package test.bugs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	Test961.class,
	Test997.class,
	Test1263.class,
})
@RunWith(Suite.class)
public class BugsSuite {
}
