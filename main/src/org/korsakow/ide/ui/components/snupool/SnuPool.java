package org.korsakow.ide.ui.components.snupool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.pool.AbstractHeader;
import org.korsakow.ide.ui.components.pool.AbstractPool;
import org.korsakow.ide.ui.components.pool.Content;

public class SnuPool extends AbstractPool<SnuEntry, KeywordEntry>
{
	@Override
	protected Content<KeywordEntry> createContent(SnuEntry entry)
	{
		Content<KeywordEntry> content = new Content<KeywordEntry>();
		content.setCellRenderer(new KeywordCellRenderer());
		return content;
	}
	@Override
	protected AbstractHeader createHeader(SnuEntry entry)
	{
		AbstractHeader header = new AbstractHeader(entry.getSnuName());
		return header;
	}
	@Override
	protected void updateEntry(KCollapsiblePane pane, AbstractHeader header, Content<KeywordEntry> content, SnuEntry snuEntry)
	{
		HashMap<String, KeywordEntry> map = new HashMap<String, KeywordEntry>();
		for (String keyword : snuEntry.getInKeywords())
		{
			KeywordEntry keywordEntry = map.get(keyword);
			if (keywordEntry == null) {
				keywordEntry = new KeywordEntry(keyword);
				map.put(keyword, keywordEntry);
			}
			keywordEntry.setInKeyword(true);
		}
		
		for (String keyword : snuEntry.getOutKeywords())
		{
			KeywordEntry keywordEntry = map.get(keyword);
			if (keywordEntry == null) {
				keywordEntry = new KeywordEntry(keyword);
				map.put(keyword, keywordEntry);
			}
			keywordEntry.setOutKeyword(true);
		}
		
		List<KeywordEntry> entries = new ArrayList<KeywordEntry>();
		for (KeywordEntry entry : map.values())
			entries.add(entry);
		Collections.sort(entries);
		content.setModel(entries);
	}
}
