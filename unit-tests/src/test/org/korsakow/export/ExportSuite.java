package test.org.korsakow.export;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestExporter.class,
	TestXMLExportTask.class,
})
@RunWith(Suite.class)
public class ExportSuite {
}
