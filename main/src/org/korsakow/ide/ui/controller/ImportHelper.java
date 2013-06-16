package org.korsakow.ide.ui.controller;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;

/**
 * One of the main reasons for orignially creating this class was that in order to create the folder structure we need to
 * run through some DO's but we have this situation with the two sets of DO's so I isolated this code here.
 * That said, this could be a good place to refactor controller-level import related code to.
 * 
 * @author d
 *
 */
public class ImportHelper
{
	public static void createK3FolderStructure(ResourceTreeTableModel model, K3Importer importer)
	{
		Map<String, FolderNode> nodeMap = new HashMap<String, FolderNode>();
		IProject project = importer.getProject();

		Collection<IMedia> media = project.getMedia();
		Collection<ISnu> snus = project.getSnus();
		for (ISnu snu : snus)
		{
			media.remove(snu.getMainMedia());
			createAndAddNode(nodeMap, model, snu.getMainMedia().getFilename(), snu);
		}
		for (IMedia medium : media)
		{
			createAndAddNode(nodeMap, model, medium.getFilename(), medium);
		}
		for (IInterface interf : project.getInterfaces())
		{
			model.appendNode( ResourceNode.create( interf ), model.getRoot() );
		}
	}
	private static void createAndAddNode(Map<String, FolderNode> nodeMap, ResourceTreeTableModel model, String path, IResource resource)
	{
		FolderNode root = model.getRoot();
		
		KNode resourceNode = model.findResource(resource.getId());
		if (resourceNode == null)
			resourceNode = ResourceNode.create(resource);
		
		FolderNode parentFolder = null;
		
		File file = new File(path);
		File parentFile = file.getParentFile();
		if (parentFile != null) {
			parentFolder = nodeMap.get(parentFile.getAbsolutePath());
			if (parentFolder == null) {
				parentFolder = new FolderNode(parentFile.getName());
				model.appendNode(parentFolder, root);
				nodeMap.put(parentFile.getAbsolutePath(), parentFolder);
			}
		}
		if (parentFolder == null)
			parentFolder = root;
		
		model.appendNode(resourceNode, parentFolder);
	}
}
