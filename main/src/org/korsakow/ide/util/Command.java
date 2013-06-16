package org.korsakow.ide.util;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.task.IWorker;

/**
 * Helper between application and webservice. Handles serialization/deserialization.
 * 
 * Requests from the application to the domain layer are serialized as if (perhaps) going through a webservice, but the converse is not true:
 * 	responses from the domain layer are accessed directly by the application.
 *
 * 
 * TODO: currently we serialize the entire resource for most messages, even when just the id would suffice.
 * 
 * @deprecated
 * @author dave
 *
 */
@Deprecated
public class Command
{
	private static final String CMD_IMPORTK3 = "org.korsakow.domain.command.ImportK3Command";
	private static final String CMD_LIST_SETTINGS = "org.korsakow.domain.command.ListSettingsCommand";
	
	private static final String CMD_LIST_KEYWORD = "org.korsakow.domain.command.ListKeywordCommand";
	private static final String CMD_LIST_SNU_KEYWORD = "org.korsakow.domain.command.ListSnuKeywordCommand";
	
	private static final String CMD_FIND_SNUABLE_MEDIA_NOT_USED_AS_SNU_MAIN_MEDIA = "org.korsakow.domain.command.FindSnuableMediaNotUsedAsSnuMainMediaCommand";
	private static final String CMD_FIND_RESOURCE_BY_CLICKSOUND = "org.korsakow.domain.command.FindResourceByClickSoundIdCommand";
	private static final String CMD_FIND_RESOURCE_BY_BACKGROUNDSOUND = "org.korsakow.domain.command.FindResourceByBackgroundSoundIdCommand";
	private static final String CMD_FIND_RESOURCE_BY_PREVIEWMEDIA = "org.korsakow.domain.command.FindResourceByPreviewMediaIdCommand";
	private static final String CMD_COUNT_SNU_BY_MAIN_MEDIA = "org.korsakow.domain.command.CountSnuByMainMediaIdCommand";
	private static final String CMD_FIND_SNU_BY_MAIN_MEDIA = "org.korsakow.domain.command.FindSnuByMainMediaIdCommand";
	private static final String CMD_FIND_SNU_BY_IN_KEYWORD = "org.korsakow.domain.command.FindSnuByInKeywordCommand";
	private static final String CMD_FIND_SNU_BY_OUT_KEYWORD = "org.korsakow.domain.command.FindSnuByOutKeywordCommand";
	private static final String CMD_COUNT_SNU_BY_IN_KEYWORD = "org.korsakow.domain.command.CountSnuByInKeywordCommand";
	private static final String CMD_COUNT_SNU_BY_OUT_KEYWORD = "org.korsakow.domain.command.CountSnuByOutKeywordCommand";
	private static final String CMD_SIMULATED_SEARCH = "org.korsakow.domain.command.SimulatedSearchCommand";
	
	private static Response call(String commandName, Request request)
	{
		return call(commandName, request, true);
	}
	/**
	 * Calls a command with request as arguments. All other callXXX methods delegate here.
	 * @param request the parameters to the command
	 */
	private static Response call(String commandName, Request request, boolean newUoW)
	{
		Response response = new Response();
		try {
//			Logger.getLogger(Command.class).info("Command.call: " + commandName);
			if (!request.has("project_id")) {
				IProject project = ProjectInputMapper.find();
				request.set("project_id", project.getId());
			}
			Class<AbstractCommand> commandClass = (Class<AbstractCommand>)Class.forName(commandName);
			CommandExecutor.executeCommandNoCommit(commandClass, request, response);
			
			if (newUoW)
				UoW.newCurrent();
		} catch (CommandException e) {
        	Logger.getLogger(Command.class).error("", e);
        	throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
        	Logger.getLogger(Command.class).error("", e);
        	throw new RuntimeException(e);
		} catch (MapperException e) {
			throw new RuntimeException(e);
		}
		
//		Runnable runnable = new Runnable() {
//			public void run() {
//				try {
//					//UoW.getCurrent().commit();
//					//UoW.newCurrent();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (KeyNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (CreationException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (MapperException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		};
//		UIUtil.runUITask(runnable);
		return response;
	}
	

	public static Collection<?> findResourceByClickSoundId(long id)
	{
		Request request = new Request();
		request.set("id", id);
		Response response = call(CMD_FIND_RESOURCE_BY_CLICKSOUND, request);
		List<IResource> domain = (List<IResource>)response.get("resources");
		return domain;
	}
	public static Collection<?> findResourceByBackgroundSoundId(long id)
	{
		Request request = new Request();
		request.set("id", id);
		Response response = call(CMD_FIND_RESOURCE_BY_BACKGROUNDSOUND, request);
		List<IResource> domain = (List<IResource>)response.get("resources");
		return domain;
	}
	
	public static Collection<?> findResourceByPreviewMediaId(long id)
	{
		Request request = new Request();
		request.set("id", id);
		Response response = call(CMD_FIND_RESOURCE_BY_PREVIEWMEDIA, request);
		List<IResource> domain = (List<IResource>)response.get("resources");
		return domain;
	}
	
	public static Pair<IWorker, K3Importer> importK3(String filename)
	{
		Request request = new Request();
		request.set("filename", filename);
		Response response = call(CMD_IMPORTK3, request);
		IWorker worker = (IWorker)response.get("worker");
		K3Importer importer = (K3Importer)response.get("importer");
		return new Pair<IWorker, K3Importer>(worker, importer);
	}
	
}
