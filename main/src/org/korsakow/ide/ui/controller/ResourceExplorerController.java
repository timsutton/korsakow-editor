package org.korsakow.ide.ui.controller;

import javax.swing.table.TableModel;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.DefaultResourceTreeTableModel;
import org.korsakow.ide.ui.resourceexplorer.ResourceBrowser;
import org.korsakow.ide.util.Command;


public class ResourceExplorerController extends ApplicationAdapter
{
	private final ResourceBrowser resourceBrowser;
	public ResourceExplorerController(ResourceBrowser resourceExplorer)
	{
		resourceBrowser = resourceExplorer;
		
		resourceExplorer.addEventListener(new ResourceBrowserController(resourceExplorer));
	}
	@Override
	public void onResourceAdded(IResource resource)
	{
		Application.getInstance().enqueueCommonTask(new NotifyKeywordsChangedTask());
		Application.getInstance().enqueueCommonTask(new ResourceModifiedTask());
	}
	@Override
	public void onResourceDeleted(final IResource resource)
	{
		Application.getInstance().enqueueCommonTask(new NotifyKeywordsChangedTask());
		Application.getInstance().enqueueCommonTask(new ResourceModifiedTask());
	}
	@Override
	public void onResourcesCleared()
	{
		resourceBrowser.getResourceTreeTable().setTreeTableModel(new DefaultResourceTreeTableModel(new FolderNode("/")));
		Application.getInstance().notifyKeywordsChanged();
	}
	@Override
	public void onResourceModified(IResource resource)
	{
		Application.getInstance().enqueueCommonTask(new NotifyKeywordsChangedTask());
		Application.getInstance().enqueueCommonTask(new ResourceModifiedTask());
	}
	@Override
	public void onKeywordsChanged()
	{
		Application.getInstance().enqueueCommonTask(new KeywordsChangedTask());
	}
	private class ResourceModifiedTask implements Runnable
	{
		public void run()
		{

			TableModel resourceModel = resourceBrowser.getResourceTreeTable().getModel();
			for (int row = 0; row < resourceModel.getRowCount(); ++row) {
				KNode node = (KNode)resourceBrowser.getResourceTreeTable().getValueAt(row, 0);
				if (node instanceof ResourceNode == false)
					continue;
				ResourceNode resourceNode = (ResourceNode)node;
				IResource res;
				try {
					res = ResourceInputMapper.map( resourceNode.getResourceId() );
				} catch (MapperException e) {
					Application.getInstance().showUnhandledErrorDialog( e );
					continue;
				}
				
				if (resourceNode.getResourceId() == null ||
					res == null) // not sure if would happen but a NPE may have occurred here 2009-03-27
					continue;

				resourceNode.setName(res.getName());
				
				switch (resourceNode.getResourceType()) {
				case SNU:
					ISnu snu = (ISnu) res;
					resourceNode.setEndSnu(snu.getEnder());
					resourceNode.setStartSnu(snu.getStarter());
					resourceNode.setLives(snu.getLives());
					resourceNode.setInterfaceName(snu.getInterface().getName());
					resourceNode.setFilename(snu.getMainMedia().getFilename());
					resourceNode.setPreview(snu.getPreviewMedia() != null);
					resourceNode.setBgSound(snu.getBackgroundSound() != null);
					break;
				
				case INTERFACE:
					IInterface interf = (IInterface)res;
					resourceNode.setClickSound(interf.getClickSound() != null);
					break;
				
				case SOUND:{
					boolean isClickSound = 0!=Command.findResourceByClickSoundId(resourceNode.getResourceId()).size();
					resourceNode.setClickSound(isClickSound);
					
					boolean isBackgroundSound = 0!=Command.findResourceByBackgroundSoundId(resourceNode.getResourceId()).size();
					resourceNode.setBgSound(isBackgroundSound);

					boolean isPreview = 0!=Command.findResourceByPreviewMediaId(resourceNode.getResourceId()).size();
					resourceNode.setPreview(isPreview);
					}break;
					
				case VIDEO:
				case IMAGE:
				case TEXT:
					boolean isPreview = 0!=Command.findResourceByPreviewMediaId(resourceNode.getResourceId()).size();
					resourceNode.setPreview(isPreview);
					break;
				}
				
				resourceBrowser.getResourceTreeTable().getTreeTableModel().fireChanged(resourceNode);
			}
			
		}
	}
	private class NotifyKeywordsChangedTask implements Runnable
	{
		public void run()
		{
			Application.getInstance().notifyKeywordsChanged();
		}
	}
	private class KeywordsChangedTask implements Runnable
	{
		public void run()
		{
			// todo: remove me since no longer used
		}
	}
}
