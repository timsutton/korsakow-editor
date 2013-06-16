package test.org.korsakow.encoding;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestQTMediaInfo.class,
	TestFFMpegMediaInfo.class,
	TestVideoExport.class,
})
@RunWith(Suite.class)
public class EncodingSuite {
}
