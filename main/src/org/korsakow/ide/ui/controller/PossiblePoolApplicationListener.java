/**
 * 
 */
package org.korsakow.ide.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resources.UnusedMediaResourceTreeTableModel;

public class PossiblePoolApplicationListener extends ApplicationAdapter implements Runnable, PropertyChangeListener
{
	private final ResourceTreeTable treeTable;
	public PossiblePoolApplicationListener(ResourceTreeTable treeTable)
	{
		this.treeTable = treeTable;
	}
	public void run()
	{
		doUpdate();
	}
	private void doUpdate()
	{
		((UnusedMediaResourceTreeTableModel)treeTable.getTreeTableModel()).update();
	}
	private void enqueueUpdate()
	{
		Application.getInstance().enqueueCommonTask(this);
	}
	@Override
	public void onProjectLoaded(IProject project) {
		enqueueUpdate();
	}
	@Override
	public void onResourceAdded(IResource resource) {
		enqueueUpdate();
	}
	@Override
	public void onResourceDeleted(IResource resource) {
		enqueueUpdate();
	}
	@Override
	public void onResourceModified(IResource resource) {
		enqueueUpdate();
	}
	@Override
	public void onResourcesCleared() {
		enqueueUpdate();
	}
	@Override
	public void onKeywordsChanged() {
		enqueueUpdate();
	}

	/**
	 * Listening for property changes on not this.treeTable but ResourceBrowser.ResourceTreeTable
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("model")) {
			boolean includeImages = ((UnusedMediaResourceTreeTableModel)treeTable.getTreeTableModel()).isIncludeImages();
			boolean includeVideos = ((UnusedMediaResourceTreeTableModel)treeTable.getTreeTableModel()).isIncludeVideos();
			treeTable.setTreeTableModel(new UnusedMediaResourceTreeTableModel(treeTable, Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel(), includeImages, includeVideos));
		}
	}
}