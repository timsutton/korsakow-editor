package org.korsakow.ide.controller;

import java.awt.event.WindowEvent;

import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;

public class ApplicationAdapter implements ApplicationListener
{
	public void onProjectLoaded(IProject project) {
	}
	public void onResourceAdded(IResource resource) {
	}
	public void onResourceDeleted(IResource resource) {
	}
	public void onResourceModified(IResource resource) {
	}
	public void onResourcesCleared() {
		
	}
	public void onKeywordsChanged() {
	}
	public void onWindowActivated(WindowEvent event) {
	}
	public void onWindowClosed(WindowEvent event) {
	}
	public boolean onApplicationWillShutdown() {
		return true;
	}
	public void onApplicationShutdown() {
		
	}
}
