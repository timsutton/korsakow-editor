/**
 * 
 */
package org.korsakow.ide.ui.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.ui.settings.MovieSettingsPanel;

public class MovieSettingsPanelController
{
	private static class BackgroundColorListener implements ActionListener
	{
		private final MovieSettingsPanel movieSettingsPanel;
		public BackgroundColorListener(MovieSettingsPanel movieSettingsPanel)
		{
			this.movieSettingsPanel = movieSettingsPanel;
		}
		public void actionPerformed(ActionEvent e)
		{
			Color initialColor = movieSettingsPanel.getBackgroundColor();
			if (initialColor == null)
				initialColor = Color.black;
			final JColorChooser chooser = ColorPropertyHandler.createColorChooser(initialColor);
			
			ActionListener okListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					movieSettingsPanel.setBackgroundColor(chooser.getColor());
				}
			};
			ActionListener cancelListener = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
				}
			};
			final JDialog dialog = JColorChooser.createDialog(movieSettingsPanel, "Color", true, chooser, okListener, cancelListener);
			dialog.setVisible(true);
		}
	}
	MovieSettingsPanel movieSettingsPanel;
	public MovieSettingsPanelController(MovieSettingsPanel movieSettingsPanel)
	{
		this.movieSettingsPanel = movieSettingsPanel;
		movieSettingsPanel.addBackgroundColorActionListener(new BackgroundColorListener(movieSettingsPanel));
	}
}
