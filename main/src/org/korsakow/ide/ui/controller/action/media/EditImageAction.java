/**
 * 
 */
package org.korsakow.ide.ui.controller.action.media;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.controller.AbstractMediaEditAction;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.ImageResourceEditorController;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resources.ImageResourceView;

public class EditImageAction extends AbstractMediaEditAction
{
	@Override
	protected void initViewHelper(ResourceEditor editor, IResource resource) throws Exception
	{
		edit(editor, (IImage)resource);
	}
	private static void edit(ResourceEditor resourceEditor, IImage media) throws MapperException
	{
		Long id = media!=null?media.getId():null;
		ImageResourceView resourceView = new ImageResourceView();
		resourceEditor.setResourceView(resourceView, ResourceType.IMAGE);
		ImageResourceEditorController controller = new ImageResourceEditorController(resourceEditor, id); // it might be a bad sign that its unnecessary to keep this reference
		resourceEditor.addSaveActionListener( id!=null? SaveImageAction.update(resourceView, id):SaveImageAction.insert(resourceView) );
		if (media != null) {
			MediaHelper.initView(resourceView, media);
			resourceView.setDuration(media.getDuration()!=null?media.getDuration():5000);
		}
	}
}
