package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.DeleteResourceCommand;
import org.korsakow.domain.command.FindResourcesReferencingCommand;
import org.korsakow.domain.command.RemoveReferencesToResourceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.NodeVisitor;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.ResourceBrowserController;
import org.korsakow.ide.ui.dialogs.EditingConflictDialog;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.UIUtil;

public class DeleteAction implements ActionListener, KeyListener
{
	private final ResourceTreeTable treeTable;
	public DeleteAction(ResourceTreeTable treeTable)
	{
		this.treeTable = treeTable;

	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		try {
			app.stopCommonTasks();
			app.beginBusyOperation();
			List<? extends KNode> selectedNodes = treeTable.getSelectedNodes();
			deleteNodes(treeTable.getTreeTableModel(), selectedNodes);
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
		} finally {
			app.endBusyOperation();
			app.startCommonTasks();
		}
	}
	public static void deleteNodes(final ResourceTreeTableModel model, Collection<? extends KNode> nodesToDelete1) throws CommandException, MapperException
	{
		final Collection<KNode> nodesToDelete = new ArrayList<KNode>();
		for (KNode node: nodesToDelete1) {
			if ( node instanceof ResourceNode) {
				IResource resource = ResourceInputMapper.map( ((ResourceNode)node).getResourceId() );
				if ( resource.equals( ProjectInputMapper.find().getDefaultInterface() ) ) {
					Application.getInstance().showAlertDialog( LanguageBundle.getString("general.messages.cannotdeletedefaultinterface.title"), LanguageBundle.getString("general.messages.cannotdeletedefaultinterface.message") );
					continue;
				}
			}
			nodesToDelete.add(node);
		}
		Map<IResource, UseContext> resourcesInUse = new HashMap<IResource, UseContext>(); // master list
		
		Collection<ResourceNode> resourceNodesToDelete = ResourceBrowserController.filterResourceNodes(nodesToDelete);
		Set<IResource> resourcesToDelete = ResourceNode.getResources(resourceNodesToDelete);
		Set<ResourceNode> resourcesToAutomaticallyBreakLinks = new HashSet<ResourceNode>();
		for (ResourceNode node : resourceNodesToDelete) {
			if (!testDelete(node, resourcesInUse)) {
				int inUseAndAlsoToDeleteCount = 0;
				final IResource resource = ResourceInputMapper.map(node.getResourceId());
				final UseContext references = resourcesInUse.get(resource);
				if ( references != null) {
					for (IResource inUse : references.getReferences()) {
						if ( resourcesToDelete.contains(inUse) ) {
							++inUseAndAlsoToDeleteCount;
						}
					}
					if (inUseAndAlsoToDeleteCount == references.getReferences().size()) {
						resourcesToAutomaticallyBreakLinks.add(node);
					}
				}
			}
		}
		
		for (ResourceNode node : resourcesToAutomaticallyBreakLinks) {
			final IResource resource = ResourceInputMapper.map(node.getResourceId());
			Request request = new Request();
			request.set("id", node.getResourceId());
			Response response = CommandExecutor.executeCommand( RemoveReferencesToResourceCommand.class, request );
			if ( response.getCollection("lockedBy", IResource.class).isEmpty() ) {
				resourcesInUse.remove(resource);
			}
		}
		
		if (!resourcesInUse.isEmpty()) {
			showConflictsDialog(resourcesInUse, model, nodesToDelete);
		} else {
			for (ResourceNode node: resourceNodesToDelete) {
				IResource resource = ResourceInputMapper.map( node.getResourceId() );
				// delete should not fail at this point since we've checked for in-use resources, but...
				if ( delete(resource) ) {
					for ( IResource key : resourcesInUse.keySet() ) {
						UseContext ctx = resourcesInUse.get(key);
						ctx.getReferences().remove(resource);
					}
					
					// removing unrooted nodes via the model is... problematic (throws a NPE)
					if (UIUtil.isRooted(model.getRoot(), node))
						model.removeNodeFromParent(node);
				}
			}
			// cleanup folder nodes that are now empty
			for (KNode toDelete: nodesToDelete) {
				if (toDelete instanceof FolderNode == false)
					continue;
				KNode.visitDepthFirstLeafFirst(toDelete, new NodeVisitor() {
					public void visit(KNode node) {
						if (node.isLeaf() && node instanceof FolderNode) {
							if (node != node.getRoot())
								if (UIUtil.isRooted(model.getRoot(), node))
									model.removeNodeFromParent(node);
						}
					}
				});
			}
		}
	}
	private static boolean testDelete(ResourceNode removeNode, Map<IResource, UseContext> resourcesInUse) throws CommandException, MapperException
	{
		Long id = (removeNode).getResourceId();
		assert id != null;
		IResource resource = ResourceInputMapper.map( id );
		if (resource == null)
			resource = null;
		assert resource != null;
		ResourceEditor editor = Application.getInstance().getOpenEditor(ResourceInputMapper.map(resource.getId()));
		if (editor != null) {
			resourcesInUse.put(resource, new UseContext(editor));
			return false;
		}
		Collection<IResource> references = findReferences(resource);
		if (!references.isEmpty())
			resourcesInUse.put(resource, new UseContext(references));
		return references.isEmpty();
	}
	private static Collection<IResource> findReferences(IResource resource) throws CommandException
	{
		Request request = new Request();
		request.set(FindResourcesReferencingCommand.ID, resource.getId());
		Response response = new Response();
		CommandExecutor.executeCommand(FindResourcesReferencingCommand.class, request, response);
		Collection<IResource> references = response.getCollection(FindResourcesReferencingCommand.REFERENCES, IResource.class);
		return references;
	}
	private static boolean delete(IResource resource) throws CommandException
	{
		Request request = new Request();
		request.set(DeleteResourceCommand.ID, resource.getId());
		Response response = new Response();
		CommandExecutor.executeCommand(DeleteResourceCommand.class, request, response);
		boolean inUse = (response.has("resourceInUse") && response.getBoolean("resourceInUse"));
		if (!inUse) {
			Application.getInstance().registerDeleted( resource );
		}
		return !inUse;
	}
	private static void showConflictsDialog(final Map<IResource, UseContext> resourcesInUse, final ResourceTreeTableModel model, final Collection<? extends KNode> nodesToDelete)
	{
		final EditingConflictDialog dialog = Application.getInstance().showEditingConflictDialog();
		Collection<IResource> keys = resourcesInUse.keySet();
		dialog.setBreakLinksAction(new BreakLinksAction(dialog, resourcesInUse, nodesToDelete, model));
		dialog.setMessage(LanguageBundle.getString("general.errors.resourceinuse.message"));
		for (IResource resource : keys) {
			UseContext ctx = resourcesInUse.get(resource);
			dialog.addConflictItem(resource, ctx.getReferences());
		}
		dialog.setVisible(true);
	}
	private static class BreakLinksAction implements ActionListener
	{
		private final EditingConflictDialog dialog;
		private final Map<IResource, UseContext> resourcesInUse;
		private final Collection<? extends KNode> nodesToDelete;
		private final ResourceTreeTableModel model;
		public BreakLinksAction(EditingConflictDialog dialog, Map<IResource, UseContext> resourcesInUse, Collection<? extends KNode> nodesToDelete, ResourceTreeTableModel model)
		{
			this.dialog = dialog;
			this.resourcesInUse = resourcesInUse;
			this.nodesToDelete = nodesToDelete;
			this.model = model;
		}
		public void actionPerformed(ActionEvent event)
		{
			Collection<IResource> locked = new HashSet<IResource>();
			try {
				for (IResource resource : resourcesInUse.keySet()) {
					UseContext ctx = resourcesInUse.get(resource);
					if (ctx.getEditor() != null) {
						if (!Application.getInstance().showOKCancelDialog(dialog, LanguageBundle.getString("confirm.resourcehasopeneditor.title"), LanguageBundle.getString("confirm.resourcehasopeneditor.message", resource.getName())))
							continue;
						ctx.getEditor().dispose();
					}
					if (!ctx.getReferences().isEmpty()) {
						Request request = new Request();
						request.set("id", resource.getId());
						Response response = CommandExecutor.executeCommand( RemoveReferencesToResourceCommand.class, request );
						if ( !response.getCollection("lockedBy", IResource.class).isEmpty() ) {
							locked.add( resource );
						} else {
							for (IResource modified : response.getCollection("references", IResource.class))
								Application.getInstance().notifyResourceModified(modified);
						}
					}
				}
				
				if ( !locked.isEmpty() ) {
					Application.getInstance().showAlertDialog( LanguageBundle.getString("general.messages.cannotbreaklinks_locked.title"), LanguageBundle.getString("general.messages.cannotbreaklinks_locked.message") );
				} else {
					dialog.dispose();
					deleteNodes(model, nodesToDelete);
				}

			} catch (Exception e) {
				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.resourceinuse.message"), e);
			}
		}
		
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e)
	{
		if (UIUtil.isPlatformCommandKeyDown(e) && e.getKeyCode()==KeyEvent.VK_BACK_SPACE)
			actionPerformed(null);
	}
	public void keyTyped(KeyEvent e){}
}

class UseContext
{
	private final Collection<IResource> references;
	private final ResourceEditor editor;
	public UseContext(ResourceEditor editor)
	{
		this(editor, new ArrayList<IResource>());
	}
	public UseContext(Collection<IResource> references)
	{
		this(null, references);
	}
	public UseContext(ResourceEditor editor, Collection<IResource> references)
	{
		this.editor = editor;
		this.references = references;
	}
	public ResourceEditor getEditor() {
		return editor;
	}
	public Collection<IResource> getReferences() {
		return references;
	}
}
