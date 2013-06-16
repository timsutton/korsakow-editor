/**
 * 
 */
package org.korsakow.ide.ui.resources;

import java.util.Collection;
import java.util.HashSet;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.FindSnuableMediaNotUsedAsSnuMainMediaCommand;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.DefaultResourceTreeTableModel;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.UIUtil;

public class UnusedMediaResourceTreeTableModel extends DefaultResourceTreeTableModel
{
	private final ResourceTreeTableModel delegate;
	private final ResourceTreeTable treeTable;
	private boolean includeImages = false;
	private boolean includeVideos = true;
	public UnusedMediaResourceTreeTableModel(final ResourceTreeTable treeTable, ResourceTreeTableModel delegate, boolean includeImages, boolean includeVideos)
	{
//			super((FolderNode)copy(delegate.getRoot()));
		super(new FolderNode("/"));
		update();
		if (delegate == null)
			throw new NullPointerException();
		this.delegate = delegate;
		this.treeTable = treeTable;
		this.includeImages = includeImages;
		this.includeVideos = includeVideos;
//			setColumnIdentifiers(Arrays.asList(LanguageBundle.getString("resourcebrowser.columns.name.label")));
		delegate.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				update();
			}
			public void treeNodesInserted(TreeModelEvent e) {
				update();
			}
			public void treeNodesRemoved(TreeModelEvent e) {
				update();
			}
			public void treeStructureChanged(TreeModelEvent e) {
				update();
			}
		});
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				treeTable.expandAllRecursive();
			}
		});
	}
	public void update()
	{
		Application.getInstance().enqueueCommonTask(new Runnable() {
			public void run() {
				Collection<Long> unusedMedia = new HashSet<Long>();
				try {
					Response response = CommandExecutor.executeCommand(FindSnuableMediaNotUsedAsSnuMainMediaCommand.class);
					for (IMedia media : (Collection<IMedia>)response.get("media"))
						unusedMedia.add(media.getId());
				} catch (CommandException e) {
					Logger.getLogger(getClass()).error("", e);
				}
				KNode copy = copy(unusedMedia, delegate.getRoot());
				removeEmptyFolders(copy);
				setRoot(copy);
				treeTable.expandAllRecursive();
			}
		});
	}
	private static void removeEmptyFolders(KNode node)
	{
		// children first is necessary
		for (KNode child : node)
			removeEmptyFolders(child);
		
		Collection<KNode> toRemove = new HashSet<KNode>();
		for (KNode child : node) {
			if (child instanceof FolderNode && child.getChildCount() == 0)
				toRemove.add(child);
			removeEmptyFolders(child);
		}
		for (KNode child : toRemove)
			node.remove(child);
	}
	private KNode copy(Collection<Long> unusedMedia, KNode node)
	{
		KNode copy = null;
		if (node instanceof FolderNode)
			copy = copyFolderNode((FolderNode)node);
		if (node instanceof ResourceNode)
			copy = copyResourceNode(unusedMedia, (ResourceNode)node);
		if (copy != null) {
			for (KNode child : node) {
				KNode childCopy = null;
				childCopy = copy(unusedMedia, child);
				if (childCopy != null)
					copy.add(childCopy);
			}
		}
		return copy;
	}
	private ResourceNode copyResourceNode(Collection<Long> unusedMedia, ResourceNode resourceNode)
	{
		if (!unusedMedia.contains(resourceNode.getResourceId()))
			return null;
		if (!resourceNode.getResourceType().isMedia())
			return null;
		switch (resourceNode.getResourceType()) {
		case VIDEO: if (!includeVideos) return null; break;
		case IMAGE: if (!includeImages) return null; break;
		}
		return resourceNode.copy();
	}
	private static FolderNode copyFolderNode(FolderNode folderNode)
	{
		FolderNode copy = new FolderNode(folderNode.getName());
		return copy;
	}
	public boolean isIncludeImages()
	{
		return includeImages;
	}
	public void setIncludeImages(boolean includeImages)
	{
		this.includeImages = includeImages;
	}
	public boolean isIncludeVideos()
	{
		return includeVideos;
	}
	public void setIncludeVideos(boolean includeVideos)
	{
		this.includeVideos = includeVideos;
	}
}