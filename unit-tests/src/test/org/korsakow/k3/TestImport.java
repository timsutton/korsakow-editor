package test.org.korsakow.k3;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.domain.task.ITask;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.Task;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.task.UIWorker;
import org.korsakow.ide.util.FileUtil;
import org.w3c.dom.Document;

import test.util.BaseTestCase;
import test.util.Signal;

import com.sun.swingx.SwingWorker.StateValue;

/**
 * So far these just test that the import doesn't fail by throwing some exception,
 * we don't currently validate the resultant project.
 * 
 * TODO: validate the imported projects (hoo boy, have fun with THAT one!)
 * 
 * @author d
 *
 */
public class TestImport extends BaseTestCase
{
	public static class UoWTask extends AbstractTask
	{
		private final Task innerTask;
		public UoWTask(Task task)
		{
			innerTask = task;
		}
		@Override
		public void runTask() throws TaskException, InterruptedException
		{
			if (UoW.getCurrent() == null)
				UoW.newCurrent();
			DataRegistry.safeRollback();
			innerTask.run();
			try {
				UoW.getCurrent().commit();
			} catch (SQLException e) {
				throw new TaskException(e);
			} catch (KeyNotFoundException e) {
				throw new TaskException(e);
			} catch (CreationException e) {
				throw new TaskException(e);
			} catch (MapperException e) {
				throw new TaskException(e);
			}
			UoW.newCurrent();
		}

		@Override
		public String getTitleString() {
			return innerTask.getTitleString();
		}
	}
	public static final String TESTFILES_BASE = "resources/k3films";
	private File baseFile;
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		baseFile = FileUtil.createTempDirectory("k3import", "korsakow");
		baseFile.deleteOnExit();
		
		Application.initializeInstance();
	}
	@Override
	@After
	public void tearDown()
	{
		Application.destroyInstance();
	}
	
	/**
	 * caught the fact that the ###LOOP### construct breaks imports.
	 * @throws Exception
	 */
	@Test public void testImport7Sons() throws Exception
	{
		commonImport("7sons");
	}
	/**
	 *  uses many link interface. initially caught a problem where manylinks interface was given
	 *  a dynamic attribute it didn't support.
	 * @throws Exception
	 */
	@Test public void testImportMauerExpedition() throws Exception
	{
		commonImport("mauerexpedition09");
	}
	@Test public void testImportForgottenFlags() throws Exception
	{
		commonImport("forgotten-flags");
	}
	/**
	 * Keyword of length 1, unmodified, not recognized as valid
	 * @throws Exception
	 */
	@Test public void testIssue991() throws Exception
	{
		commonImport("issue991");
	}
	private void commonImport(String filename) throws Exception
	{
		Document document = DataRegistry.createDefaultEmptyDocument();

		File datafile = File.createTempFile("k3import", ".krw", baseFile);
	    DataRegistry.initialize(document, datafile);
		
		File k3file = new File(TESTFILES_BASE + File.separatorChar + filename + File.separatorChar + "data");
		
		K3Importer k3Importer = new K3Importer(k3file);
		
		List<Task> importTasks = k3Importer.createImportTasks();

		List<ITask> uowTasks = new ArrayList<ITask>();
		for (Task task : importTasks)
			uowTasks.add(new UoWTask(task));
		IWorker importWorker = new UIWorker(uowTasks);

		final Signal signal = new Signal(60*1000); // timeout may be changed as appropriate
		
		importWorker.addPropertyChangeListener("state", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getNewValue() == StateValue.DONE)
					signal.fire();
			}
		});
		
		importWorker.execute();

		signal.waitOrThrow();
	}
}
