/**
 * 
 */
package org.korsakow.ide.ui.controller;

import org.korsakow.ide.ui.settings.ProjectSettingsPanel;

public class ProjectSettingsController
{
	MovieSettingsPanelController moviePanelController;
	public ProjectSettingsController(ProjectSettingsPanel settingsPanel)
	{
		moviePanelController = new MovieSettingsPanelController(settingsPanel.getMoviePanel());
	}
	
}
