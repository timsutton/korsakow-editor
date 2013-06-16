/**
 * 
 */
package org.korsakow.ide.ui.settings;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.korsakow.ide.lang.LanguageBundle;

public class ProjectSettingsPanel extends JPanel
{
	private JTabbedPane tabbedPane;
	private MovieSettingsPanel movieSettingsPanel;
	private ExportSettingsPanel exportSettingsPanel;
	private WorkspaceSettingsPanel workspaceSettingsPanel;
	public ProjectSettingsPanel()
	{
		initUI();
	}
	protected void initUI()
	{
		setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);
		movieSettingsPanel = new MovieSettingsPanel();
		exportSettingsPanel = new ExportSettingsPanel();
		workspaceSettingsPanel = new WorkspaceSettingsPanel();
		tabbedPane.addTab(LanguageBundle.getString("projectsettings.tab.movie.label"), movieSettingsPanel);
		tabbedPane.addTab(LanguageBundle.getString("projectsettings.tab.export.label"), exportSettingsPanel);
		tabbedPane.addTab(LanguageBundle.getString("projectsettings.tab.workspace.label"), workspaceSettingsPanel);
	}
	public void dispose()
	{
		movieSettingsPanel.dispose();
		exportSettingsPanel.dispose();
		workspaceSettingsPanel.dispose();
	}
	public MovieSettingsPanel getMoviePanel()
	{
		return movieSettingsPanel;
	}
	public ExportSettingsPanel getExportPanel()
	{
		return exportSettingsPanel;
	}
	public WorkspaceSettingsPanel getWorkspaceSettingsPanel()
	{
		return workspaceSettingsPanel;
	}
}
