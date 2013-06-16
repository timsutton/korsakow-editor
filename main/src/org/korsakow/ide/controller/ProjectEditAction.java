/**
 * 
 */
package org.korsakow.ide.controller;

import org.apache.log4j.Logger;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.ProjectSettingsController;
import org.korsakow.ide.ui.controller.helper.ProjectHelper;
import org.korsakow.ide.ui.settings.ProjectSettingsPanel;

/**
 * Should not implement ResourceEditAction
 * <strike>This action is broken. Right now we don't use a ResourceEditor for Projects because ResourceEditor extends JFrame
 * but we want this window to be modal. If we make ResourceEditor a JDialog it won't show up in the taskbar in windows,
 * so pending that being acceptable, we just return null.</strike>
 * @author d
 *
 */
public class ProjectEditAction implements ResourceEditAction
{
	protected void initViewHelper(ProjectSettingsPanel view, IProject project, ISettings settings) throws Exception
	{
		ProjectHelper.edit(view, project, settings);
	}
	public ResourceEditor run(IResource resource) throws Exception
	{
		IProject project = resource.getId()!=null?ProjectInputMapper.map(resource.getId()):null;
		return run(project);
	}
	public ResourceEditor run(IProject project) throws Exception
	{
		ISettings settings = SettingsInputMapper.find(); 
		
		ProjectSettingsPanel view = new ProjectSettingsPanel();
		ProjectSettingsController controller = new ProjectSettingsController(view);
		
		try {
			initViewHelper(view, project, settings);
			
			if (!Application.getInstance().showOKCancelDialog(Application.getInstance().getProjectExplorer(), LanguageBundle.getString("projectexplorer.projectsettings.title"), view))
				return null;
			
			ProjectHelper.save(view, project!=null?project.getId():null, settings!=null?settings.getId():null);
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				view.dispose();
			} catch (Exception e) {
				Logger.getLogger(ProjectEditAction.class).error("Finally", e);
			}
		}
		
		return null;
	}
}
