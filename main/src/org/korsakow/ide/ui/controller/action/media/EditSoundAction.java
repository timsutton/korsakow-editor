/**
 * 
 */
package org.korsakow.ide.ui.controller.action.media;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.controller.AbstractMediaEditAction;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.controller.SoundResourceEditorController;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resources.SoundResourceView;

public class EditSoundAction extends AbstractMediaEditAction
{
	@Override
	protected void initViewHelper(ResourceEditor editor, IResource resource) throws Exception
	{
		edit(editor, (ISound)resource);
	}
	private static void edit(ResourceEditor resourceEditor, ISound media) throws MapperException
	{
		Long id = media!=null?media.getId():null;
		SoundResourceView resourceView = new SoundResourceView();
		resourceEditor.setResourceView(resourceView, ResourceType.SOUND);
		SoundResourceEditorController controller = new SoundResourceEditorController(resourceEditor, id); // it might be a bad sign that its unnecessary to keep this reference
		resourceEditor.addSaveActionListener( id!=null? SaveSoundAction.update(resourceView, id):SaveSoundAction.insert(resourceView) );
		if (media != null)
			MediaHelper.initView(resourceView, media);
	}
}
