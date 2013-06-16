package test.org.korsakow.action;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.Project;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.DeleteAction;
import org.korsakow.ide.ui.dialogs.EditingConflictDialog;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.StrongReference;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.Util;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DomainTestUtil;

public class TestDeleteAction extends AbstractDomainObjectTestCase
{
	private static boolean visualMode = false;
	private MockApplication mockApp;
	private JFrame frame;
	private Random random;
	private ResourceTreeTable treeTable;
	private ResourceTreeTableModel model;
	private Collection<IResource> domainList;
	private Collection<KNode> nodeList;
	private Collection<KNode> remainingNodes;
	private Collection<KNode> selectedNodes;
	@Override
	@Before
	public void setUp() throws Exception
	{
		final long SEED = System.nanoTime();
		System.out.println(String.format("Random Seed: %d", SEED)); // printed in case you want/need to repro a particular run
		random = new Random(SEED);
		
	}
	private void doInit(boolean dobreak) throws Exception {
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Throwable {
				TestDeleteAction.super.setUp();
				
				Application.initializeInstance(mockApp = new MockApplication(false));
				treeTable = new ResourceTreeTable();
				treeTable.setAutoCreateColumnsFromModel(false);
				model = treeTable.getTreeTableModel();

				if (visualMode) {
					frame = new JFrame("TestDeleteAction");
					frame.setSize(480, 640);
					frame.add(new JScrollPane(treeTable));
					frame.pack();
					frame.setVisible(true);
					frame.toFront();
				}
			}
		});
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Throwable {
				if (visualMode) {
					frame.dispose();
					frame = null;
				}
				Application.destroyInstance();

				TestDeleteAction.super.tearDown();
			}
		});
		treeTable = null;
		model = null;
		domainList = null;
		nodeList = null;
		selectedNodes = null;
	}
	@Test public void testDeleteEntireNonReferencingHierarchy() throws Throwable
	{
		doInit(true);
		
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				createRandomHierarchy();
			}
		});
		sleep(1000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				selectedNodes = selectAll();
				remainingNodes = getRemainingNodes(nodeList, selectedNodes);
				Assert.assertTrue(remainingNodes.isEmpty());
			}
		});
		sleep(2000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				DeleteAction deleteAction = new DeleteAction(treeTable);
				deleteAction.actionPerformed(null);
			}
		});
		sleep(2000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				assertDeleted(treeTable.getTreeTableModel(), Util.filterList(selectedNodes, new Util.Predicate<KNode>() {
					public boolean execute(KNode node) {
						return node instanceof ResourceNode &&
							((ResourceNode)node).getResourceType() == ResourceType.SNU;
					}
				}));
				assertNotDeleted(treeTable.getTreeTableModel(), remainingNodes);
			}
		});
	}
	@Test public void testDeleteSubSelectionOfNonReferencingHierarchy() throws Throwable
	{
		doInit(true);
		
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				createRandomHierarchy();
			}
		});
		sleep(1000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				selectedNodes = selectRandomIntervals();
				remainingNodes = getRemainingNodes(nodeList, selectedNodes);
			}
		});
		sleep(2000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				DeleteAction deleteAction = new DeleteAction(treeTable);
				deleteAction.actionPerformed(null);
			}
		});
		sleep(2000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				assertDeleted(treeTable.getTreeTableModel(), Util.filterList(selectedNodes, new Util.Predicate<KNode>() {
					public boolean execute(KNode node) {
						return node instanceof ResourceNode &&
							((ResourceNode)node).getResourceType() == ResourceType.SNU;
					}
				}));
				assertNotDeleted(treeTable.getTreeTableModel(), remainingNodes);
			}
		});
	}
	@Test public void testDeleteSnuMainMedia() throws Throwable
	{
		doInit(false);
		
		final StrongReference<KNode> mediaNodeRef = new StrongReference<KNode>();
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				final Project project = ProjectFactory.createNew();
				UoW.getCurrent().registerNew( project );
				IVideo media = VideoFactory.createNew();
				DomainTestUtil.initializeRandom(media);
				ISnu snu = SnuFactory.createNew();
				DomainTestUtil.initializeRandom(snu);
				snu.setMainMedia(media);
				IInterface interf = InterfaceFactory.createNew();
				snu.setInterface(interf);
				
				UoW.getCurrent().commit();
				UoW.newCurrent();
				
//				Video appMedia = DomainToApp.domainToApp(media);
//				Snu appSnu = DomainToApp.domainToApp(snu);
				
				KNode mediaNode = ResourceNode.create( media );
				model.appendNode( mediaNode, model.getRoot() );
				
				KNode snuNode = ResourceNode.create( snu );
				model.appendNode( snuNode, model.getRoot() );
				mediaNodeRef.set(mediaNode);
				treeTable.expandAllRecursive();
				
				nodeList = new ArrayList<KNode>();
				nodeList.add(mediaNode);
				nodeList.add(snuNode);
			}
		});
		sleep(1000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				int mediaRow = treeTable.getRow(mediaNodeRef.get());
				treeTable.getSelectionModel().setSelectionInterval(mediaRow, mediaRow);
				selectedNodes = Arrays.asList(mediaNodeRef.get());
				remainingNodes = getRemainingNodes(nodeList, selectedNodes);
			}
		});
		sleep(2000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				DeleteAction deleteAction = new DeleteAction(treeTable);
				deleteAction.actionPerformed(null);
			}
		});
		sleep(2000);
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Exception {
				assertDeleted(treeTable.getTreeTableModel(), selectedNodes);
				assertNotDeleted(treeTable.getTreeTableModel(), remainingNodes);
			}
		});
	}
	
	
	private void sleep(long time) throws InterruptedException
	{
		if (visualMode)
			Thread.sleep(time);
	}
	private Collection<KNode> selectRandomIntervals() throws Exception
	{
		Assert.assertTrue(SwingUtilities.isEventDispatchThread());
		int rowCount = treeTable.getRowCount();
		List<Integer> rows = new ArrayList<Integer>();
		for (int i = 0; i < rowCount; ++i)
			rows.add(i);
		Collections.shuffle(rows);
		int N = (int)Math.ceil(rowCount / 2.0); // ceil ensures non-zero
		rows = rows.subList(0, N);
		treeTable.clearSelection();
		for (int i = 0; i < N; ++i)
			treeTable.getSelectionModel().addSelectionInterval(rows.get(i), rows.get(i));
		return (Collection<KNode>)treeTable.getSelectedNodes();
	}
	private Collection<KNode> selectAll() throws Exception
	{
		Assert.assertTrue(SwingUtilities.isEventDispatchThread());
		int rowCount = treeTable.getRowCount();
		treeTable.clearSelection();
		treeTable.getSelectionModel().addSelectionInterval(0, rowCount-1);
		return (Collection<KNode>)treeTable.getSelectedNodes();
	}
	private void createRandomHierarchy() throws Exception
	{
		IProject project = ProjectFactory.createNew();
		DomainTestUtil.initializeRandom(project);
		UoW.getCurrent().registerNew(project);
		
		Assert.assertTrue(SwingUtilities.isEventDispatchThread());
		Collection<IResource> domainObjects = new HashSet<IResource>();
		int N = 11 + random.nextInt(10);
		for (int i = 0; i < N; ++i)
		{
			int M = random.nextInt(3);
			IResource resource = null;
			switch (M)
			{
			case 0:
			case 1:
				resource = VideoFactory.createNew();
				DomainTestUtil.initializeRandom((IVideo)resource);
				break;
			case 2:
				resource = ImageFactory.createNew();
				DomainTestUtil.initializeRandom((IImage)resource);
				break;
			case 3:
				resource = SoundFactory.createNew();
				DomainTestUtil.initializeRandom((ISound)resource);
				break;
			case 4:
				resource = TextFactory.createNew();
				DomainTestUtil.initializeRandom((IText)resource);
				break;
//			case 5:
//				resource = PatternFactory.createNew();
//				DomainTestUtil.initializeRandom((IPattern)resource);
//				break;
			}
			domainObjects.add(resource);
		}
		Assert.assertFalse(domainObjects.isEmpty());
		UoW.getCurrent().commit();
		UoW.newCurrent();
		nodeList = new HashSet<KNode>();
		FolderNode currentFolder = model.getRoot();
		for (IResource obj : domainObjects)
		{
			if (random.nextBoolean()) {
				FolderNode parent = currentFolder;
				currentFolder = new FolderNode(DomainTestUtil.getRandomString());
				model.appendNode(currentFolder, parent);
			} else {
				if (random.nextBoolean())
					currentFolder = model.getRoot();
			}
			KNode node = ResourceNode.create(obj);
			model.appendNode(node, currentFolder);
			nodeList.add(node);
		}
		treeTable.expandAllRecursive();
		Assert.assertFalse(nodeList.isEmpty());
		
		domainList = domainObjects;
	}
	private static void assertDeleted(ResourceTreeTableModel model, Collection<KNode> nodes) throws Exception
	{
		for (KNode node : nodes)
		{
			// ui layer
			Assert.assertTrue(model.isValidTreeTableNode(node));
			if (node instanceof ResourceNode) {
				ResourceNode resourceNode = (ResourceNode)node;
				// domain layer
				ResourceInputMapper.map(resourceNode.getResourceId());
			}
		}
	}
	private static void assertNotDeleted(ResourceTreeTableModel model, Collection<KNode> nodes) throws Exception
	{
		for (KNode node : nodes)
		{
			// ui layer
			Assert.assertTrue(node.getClass().getSimpleName() + ": " + node.getName(), model.isValidTreeTableNode(node));
			if (node instanceof ResourceNode) {
				ResourceNode resourceNode = (ResourceNode)node;
				// domain layer
				ResourceInputMapper.map(resourceNode.getResourceId());
			}
		}
	}

	/**
	 * Filters out any nodes that were deleted because their parent was selected.
	 * @param fullList
	 * @param deletedList
	 * @return
	 */
	private static Collection<KNode> getRemainingNodes(Collection<KNode> fullList, Collection<KNode> deletedList) {
		Collection<KNode> notDeleted = new HashSet<KNode>(fullList);
		for (KNode node : fullList) {
			if (deletedList.contains(node)) {
				notDeleted.remove(node);
				continue;
			}
			for (KNode sel : deletedList) {
				if (node.isNodeAncestor(sel)) {
					notDeleted.remove(node);
					break;
				}
			}
		}
		return notDeleted;
	}
	private static class MockApplication extends Application
	{
		public boolean alerted = false;
		public MockEditingConflictDialog dialog;
		public boolean dobreak;
		protected MockApplication(boolean dobreak) throws Exception {
			super();
			this.dobreak = dobreak;
		}

		@Override
		public EditingConflictDialog showEditingConflictDialog()
		{
			dialog = new MockEditingConflictDialog(getProjectExplorer(), dobreak);
			dialog.setTitle("Editing Conflict");
			dialog.setModal(true);
			dialog.setSize(new Dimension(320, 240));
			UIUtil.centerOnFrame(dialog, getProjectExplorer());
			return dialog;
		}
		public void showAlertDialog(String title, String message) {
			alerted = true;
		}
	}
	private static class MockEditingConflictDialog extends EditingConflictDialog
	{
		boolean dobreak;
		public MockEditingConflictDialog(JFrame parent, boolean dobreak) {
			super(parent);
			this.dobreak = dobreak;
		}
		@Override
		public void show()
		{
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					try { Thread.sleep(100); } catch (InterruptedException e) {}
					if (dobreak)
						breakLinksButton.doClick();
					else
						cancelButton.doClick();
				}
			});
			super.show();
		}
	}
}
