/**
 * 
 */
package org.korsakow.ide.ui.controller.action.media;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.controller.AbstractMediaEditAction;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.VideoResourceEditorController;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resources.VideoResourceView;

public class EditVideoAction extends AbstractMediaEditAction
{
	@Override
	protected void initViewHelper(ResourceEditor editor, IResource resource) throws Exception
	{
		edit(editor, (IVideo)resource);
	}
	private static void edit(ResourceEditor resourceEditor, IVideo media) throws MapperException
	{
		Long id = media!=null?media.getId():null;
		VideoResourceView resourceView = new VideoResourceView();
		resourceEditor.setResourceView(resourceView, ResourceType.VIDEO);
		VideoResourceEditorController controller = new VideoResourceEditorController(resourceEditor, id); // it might be a bad sign that its unnecessary to keep this reference
		resourceEditor.addSaveActionListener( id!=null? SaveVideoAction.update(resourceView, id):SaveVideoAction.insert(resourceView) );
		if (media != null)
			MediaHelper.initView(resourceView, media);
	}
}
