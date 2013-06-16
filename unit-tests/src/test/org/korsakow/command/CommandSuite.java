package test.org.korsakow.command;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestAdjustToRelativePathsCommand.class,
	TestAdjustToAbsolutePathsCommand.class,
	TestCommandExecutor.class,
	TestFindSubtitlesCommand.class,
	TestInsertCommand.class,
	TestDeleteCommand.class,
	TestDeleteResourceCommand.class,
	TestDeleteCommandWhenReferenced.class,
	TestRemoveReferencesCommand.class,
	TestSimulatedSearchCommand.class,
})
@RunWith(Suite.class)
public class CommandSuite {
}
