/**
 * 
 */
package org.korsakow.ide.ui.controller;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;

public class WindowTitleUpdater extends ApplicationAdapter
{
	@Override
	public void onResourceModified(IResource resource)
	{
		IProject project;
		try {
			project = ProjectInputMapper.find();
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			return;
		}
		if (resource.getId().equals(project.getId()))
			Application.getInstance().getProjectExplorer().setTitleExtra(project.getName(), Application.getInstance().getSaveFile()!=null?Application.getInstance().getSaveFile().getName():null);
	}
}