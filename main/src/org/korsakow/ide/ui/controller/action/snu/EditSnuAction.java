/**
 * 
 */
package org.korsakow.ide.ui.controller.action.snu;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.AbstractResourceEditAction;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.SnuResourceEditorController;
import org.korsakow.ide.ui.controller.action.ShowKeywordPoolWindowAction;
import org.korsakow.ide.ui.resources.SnuResourceView;
import org.korsakow.ide.util.UIUtil;

public class EditSnuAction extends AbstractResourceEditAction
{
	@Override
	protected void initViewHelper(ResourceEditor editor, IResource resource) throws Exception
	{
		edit(editor, (ISnu)resource);
	}
	@Override
	protected void adjustEditorAfterPack(ResourceEditor editor, IResource resource)
	{
		new ShowKeywordPoolWindowAction().actionPerformed(null);
		JFrame poolDialog = Application.getInstance().getKeywordPoolDialog();
		
		Dimension screenSize = UIUtil.getAvailableScreenSize();
		Point point = new Point();
		point.y = editor.getY();
		if (editor.getX()+editor.getWidth()+poolDialog.getWidth() > screenSize.width) {
			point.x = editor.getX()-poolDialog.getWidth();
		} else {
			point.x = editor.getX()+editor.getWidth();
		}
			
		poolDialog.setLocation(point);
	}
	private static void edit(ResourceEditor resourceEditor, ISnu snu) throws MapperException
	{
		Long id = snu!=null?snu.getId():null;
		SnuResourceView resourceView = new SnuResourceView();
		resourceEditor.setResourceView(resourceView, ResourceType.SNU);
		SnuResourceEditorController controller = new SnuResourceEditorController(resourceEditor, id); // it might be a bad sign that its unnecessary to keep this reference
		resourceEditor.addSaveActionListener( id!=null? SaveSnuAction.update(resourceView, id):SaveSnuAction.insert(resourceView) );
		if (snu != null)
			SnuHelper.initView(resourceView, snu);
		controller.validate();
	}
}
