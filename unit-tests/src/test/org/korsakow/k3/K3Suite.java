package test.org.korsakow.k3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestImport.class,
	TestImportFeatures.class,
})
@RunWith(Suite.class)
public class K3Suite {
}
