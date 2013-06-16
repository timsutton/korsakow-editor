package test.org.korsakow.action;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestDeleteAction.class,
	TestImportInterfaceAction.class,
})
@RunWith(Suite.class)
public class ActionSuite {
}
