package org.korsakow.ide.controller;

import java.awt.event.WindowEvent;
import java.util.EventListener;

import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;

public interface ApplicationListener extends EventListener
{
	void onResourceAdded(IResource resource);
	void onResourceDeleted(IResource resource);
	void onResourceModified(IResource resource);
	void onResourcesCleared();
	void onProjectLoaded(IProject project);
	void onKeywordsChanged();
	void onWindowActivated(WindowEvent event);
	void onWindowClosed(WindowEvent event);
	boolean onApplicationWillShutdown();
	void onApplicationShutdown();
}
