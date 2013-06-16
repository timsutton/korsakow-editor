/**
 * 
 */
package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;

public class MenuLanguageItemAction extends AbstractAction
{
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem)e.getSource();
		Locale locale = (Locale)item.getClientProperty("locale");
		if (locale.equals(LanguageBundle.getCurrentLocale()))
			return; // don't want to show the dialog
		
		LanguageBundle.setCurrentLocale(locale);
		
		Application.getInstance().showAlertDialog(LanguageBundle.getString("projectexplorer.menu.language.dialog.title"), LanguageBundle.getString("projectexplorer.menu.language.dialog.message"));
	}
}