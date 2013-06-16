/**
 * 
 */
package org.korsakow.ide.ui.controller.action.interf;

import java.awt.Dimension;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.AbstractResourceEditAction;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.InterfaceResourceEditorController;
import org.korsakow.ide.ui.controller.helper.InterfaceHelper;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;

public class EditInterfaceAction extends AbstractResourceEditAction
{
	@Override
	protected void initViewHelper(ResourceEditor editor, IResource resource) throws Exception
	{
		edit(editor, (IInterface)resource);
	}
	@Override
	protected void adjustEditorAfterPack(ResourceEditor editor, IResource resource)
	{
		Dimension size = editor.getSize();
		size.width = Math.max(640, size.width);
		size.height = Math.max(480, size.height);
		editor.setSize(size);
		editor.setResizable(true);
	}
	private static void edit(ResourceEditor resourceEditor, IInterface interf) throws MapperException
	{
		Long id = interf!=null?interf.getId():null;
		InterfaceBuilderMainPanel resourceView = new InterfaceBuilderMainPanel();
		resourceEditor.setResourceView(resourceView, ResourceType.INTERFACE);
		InterfaceResourceEditorController controller = new InterfaceResourceEditorController(resourceEditor, id); // it might be a bad sign that its unnecessary to keep this reference
		resourceEditor.addSaveActionListener( id!=null?SaveInterfaceAction.update(resourceView, id):SaveInterfaceAction.insert(resourceView));
		if ( id != null ) {
			resourceEditor.setSaveCopyVisible( true );
			resourceEditor.addSaveCopyActionListener(SaveInterfaceAction.copy(resourceView, id));
		}
		if (interf != null) {
			InterfaceHelper.initView(resourceView, interf);
			controller.initialState();
		}
	}
	@Override
	public ResourceEditor run(IResource resource) throws Exception {
		IProject project = ProjectInputMapper.find();

		if (resource != null &&
			project.getDefaultInterface() != null &&
			project.getDefaultInterface().getId().equals(resource.getId()))
			Application.getInstance().showOneTimeAlertDialog("SaveInterface", Application.getInstance().getProjectExplorer(), LanguageBundle.getString("interfacebuilder.editalert.title"), LanguageBundle.getString("interfacebuilder.editalert.message"));
		return super.run(resource);
	}

}