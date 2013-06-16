package org.korsakow.ide.ui.components.linkpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.SimulatedSearchCommand;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.pool.AbstractHeader;
import org.korsakow.ide.ui.components.pool.AbstractPool;
import org.korsakow.ide.ui.components.pool.Content;

public class LinkPool extends AbstractPool<SnuHeaderEntry, SnuContentEntry>
{
	@Override
	protected Content<SnuContentEntry> createContent(SnuHeaderEntry entry)
	{
		Content<SnuContentEntry> content = new SnuContent();
		content.setCellRenderer(new ContentCellRenderer());
		return content;
	}
	@Override
	protected AbstractHeader createHeader(SnuHeaderEntry entry)
	{
		AbstractHeader header = new AbstractHeader(entry.getSnuName());
		return header;
	}
	@Override
	protected void updateEntry(KCollapsiblePane pane, AbstractHeader header, Content<SnuContentEntry> content, SnuHeaderEntry headerEntry)
	{
		// TODO: do this in the model
		List<SnuContentEntry> entries = new ArrayList<SnuContentEntry>();
		Collection<ISnu> links;
		try {
			Request request = new Request();
			request.set("id", headerEntry.getSnuId());
			Response response = CommandExecutor.executeCommand(SimulatedSearchCommand.class, request);
			links = (Collection<ISnu>)response.get("results");
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			links = Collections.EMPTY_LIST;
		}
		for (ISnu link : links)
		{
			SnuContentEntry contentEntry = new SnuContentEntry(link.getId(), link.getName());
			entries.add(contentEntry);
		}
		Collections.sort(entries);
		content.setModel(entries);
	}
}
