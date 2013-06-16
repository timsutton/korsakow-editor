/**
 * 
 */
package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ProjectExplorer;

public class MenuLanguageAction extends AbstractAction
{
	private final ProjectExplorer explorer;
	public MenuLanguageAction(ProjectExplorer explorer)
	{
		this.explorer = explorer;
	}
	public void actionPerformed(ActionEvent e) {
		Collection<Locale> locales = LanguageBundle.getAvailableLocales();
		JMenu menu = (JMenu)explorer.getMenu(ProjectExplorer.Action.MenuLanguage);
		menu.removeAll();
		Locale currentLocale = LanguageBundle.getCurrentLocale();
		for (Locale locale : locales) {
			JCheckBoxMenuItem item = new JCheckBoxMenuItem(locale.getDisplayName(locale));
			item.setBackground(new JMenuItem().getBackground());
			item.addActionListener(new MenuLanguageItemAction());
			item.putClientProperty("locale", locale);
			menu.add(item);
			if (locale.equals(currentLocale))
				item.setSelected(true);
		}
	}
}