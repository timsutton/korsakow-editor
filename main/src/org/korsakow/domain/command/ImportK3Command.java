package org.korsakow.domain.command;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.domain.task.ITask;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.Task;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.task.UIWorker;
import org.w3c.dom.Document;


public class ImportK3Command extends AbstractCommand{


	public ImportK3Command(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
		throws CommandException {

		try {
		    
			Document document = DataRegistry.createDefaultEmptyDocument();

			File datafile = File.createTempFile("korsakow", ".xml");
		    DataRegistry.initialize(document, datafile);
			
			String filename = request.getString("filename");
			File k3file = new File(filename);
			
			final K3Importer k3Importer = new K3Importer(k3file);
			
			List<Task> importTasks = k3Importer.createImportTasks();
//			importTasks.add(0, new DefaultInterfacesTask(k3Importer));
//			importTasks.add(new CheckInterfacesTask(k3Importer));
			
			List<ITask> uowTasks = new ArrayList<ITask>();
			for (Task task : importTasks)
				uowTasks.add(new UoWTask(task));
			uowTasks.add(new UoWTask(new AbstractTask() {
				@Override
				public void runTask() throws TaskException {
					k3Importer.getProject().setUUID(UUID.randomUUID().toString());
					UoW.getCurrent().registerDirty(k3Importer.getProject());
				}
			}));
			IWorker importWorker = new UIWorker(uowTasks);
			
			response.set("worker", importWorker);
			response.set("importer", k3Importer);
	//		importWorker.execute();
		} catch (Exception e) {
			throw new CommandException(e);
		}

	}
	/**
	 * @deprecated this seems like a really silly thing and should be gotten rid of
	 */
	@Deprecated
	private static class UoWTask extends AbstractTask
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
}
