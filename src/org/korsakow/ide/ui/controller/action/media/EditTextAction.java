/**
 * 
 */
package org.korsakow.ide.ui.controller.action.media;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IText;
import org.korsakow.ide.controller.AbstractMediaEditAction;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.TextResourceEditorController;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resources.TextResourceView;

public class EditTextAction extends AbstractMediaEditAction
{
	@Override
	protected void initViewHelper(ResourceEditor editor, IResource resource) throws Exception
	{
		edit(editor, (IText)resource);
	}
	private static void edit(ResourceEditor resourceEditor, IText media) throws MapperException, FileNotFoundException, IOException
	{
		Long id = media!=null?media.getId():null;
		TextResourceView resourceView = new TextResourceView();
		resourceEditor.setResourceView(resourceView, ResourceType.TEXT);
		TextResourceEditorController controller = new TextResourceEditorController(resourceEditor, id); // it might be a bad sign that its unnecessary to keep this reference
		resourceEditor.addSaveActionListener( id!=null? SaveTextAction.update(resourceView, id):SaveTextAction.insert(resourceView) );
		if (media != null)
			MediaHelper.initView(resourceView, media);
	}
}
