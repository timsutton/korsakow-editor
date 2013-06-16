/**
 * 
 */
package org.korsakow.ide.ui.components.keywordpool;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.pool.Content;
import org.korsakow.ide.util.UIUtil;

public class KeywordContent extends Content<SnuEntry>
{
	public KeywordContent()
	{
		list.setCursor(new Cursor(Cursor.HAND_CURSOR));
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent event)
			{
				if (!UIUtil.isRegularDoubleClick(event))
					return;
				SnuEntry entry = (SnuEntry)list.getSelectedValue();
				try {
					Application.getInstance().edit(ResourceType.SNU, entry.getSnuId());
				} catch (Exception e) {
					Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
				}
			}
		});
	}
}