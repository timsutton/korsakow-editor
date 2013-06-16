package test.org.korsakow.service.tdg;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestInterfaceTDG.class,
	TestPredicateTDG.class
})
@RunWith(Suite.class)
public class TDGSuite {
}
