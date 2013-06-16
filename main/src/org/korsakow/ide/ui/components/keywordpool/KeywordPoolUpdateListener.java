/**
 * 
 */
package org.korsakow.ide.ui.components.keywordpool;

import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.CountSnuByInKeywordCommand;
import org.korsakow.domain.command.CountSnuByOutKeywordCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.KeywordInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.services.tdg.SnuTDG;

public class KeywordPoolUpdateListener extends ApplicationAdapter implements Runnable
{
	private final KeywordPool pool;
	public KeywordPoolUpdateListener(KeywordPool pool)
	{
		this.pool = pool;
	}
	public void run()
	{
		doUpdate();
	}
	private void update()
	{
		Application.getInstance().enqueueCommonTask(this);
	}
	private void doUpdate()
	{
		Collection<IKeyword> keywords;
		try {
			keywords = KeywordInputMapper.findByObjectTypeRecursive( SnuTDG.NODE_NAME );
		} catch (MapperException e) {
			keywords = new ArrayList<IKeyword>();
			Application.getInstance().showUnhandledErrorDialog( e );
		}
		final KeywordPoolModel model = new KeywordPoolModel();
		model.beginBatchUpdate();
		try {
			for (IKeyword keyword : keywords)
			{
				int inCount = CommandExecutor.executeCommand(CountSnuByInKeywordCommand.class, Request.single("keyword", keyword.getValue())).getInt("count");
				int outCount = CommandExecutor.executeCommand(CountSnuByOutKeywordCommand.class, Request.single("keyword", keyword.getValue())).getInt("count");
				model.add(keyword, inCount, outCount);
			}
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
		}
		model.endBatchUpdate();
		pool.setModel(model);
	}
	@Override
	public void onKeywordsChanged() {
		update();
	}
	@Override
	public void onProjectLoaded(IProject project) {
	}
	@Override
	public void onResourceAdded(IResource resource) {
		if (ResourceType.forId(resource.getType()) == ResourceType.SNU)
			update();
	}
	@Override
	public void onResourceDeleted(IResource resource) {
		if (ResourceType.forId(resource.getType()) == ResourceType.SNU)
			update();
	}
	@Override
	public void onResourceModified(IResource resource) {
		if (ResourceType.forId(resource.getType()) == ResourceType.SNU)
			update();
	}
	@Override
	public void onResourcesCleared() {
		update();
	}
	public void updateHeader()
	{
		// we walk back through the editors from most recent to least
		// and find the most recent and still open snu editor
		
		Application app = Application.getInstance();
		Collection<ResourceEditor> editors = app.getOpenEditors();
		boolean didSetText = false;
		for (ResourceEditor editor : editors) {
			IResource resource = null;
			try {
				resource = app.getResourceForEditor(editor);
			} catch (MapperException e) {
				Application.getInstance().showUnhandledErrorDialog(e);
			}
			if (resource != null) {
				if (resource instanceof ISnu) {
					didSetText = true;
					pool.setHeaderText(resource.getName());
				}
			}
		}
		if (!didSetText)
			pool.setHeaderText("");
	}
	@Override
	public void onWindowActivated(WindowEvent event)
	{
		updateHeader();
	}
	@Override
	public void onWindowClosed(WindowEvent event)
	{
		updateHeader();
	}
}