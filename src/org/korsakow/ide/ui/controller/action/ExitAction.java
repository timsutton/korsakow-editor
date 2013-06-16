package org.korsakow.ide.ui.controller.action;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.eawt.ApplicationEvent;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.dialogs.UnsavedChangesDialog;
import org.korsakow.ide.ui.dialogs.UnsavedChangesDialog.Result;
import org.korsakow.ide.util.StrongReference;
import org.korsakow.ide.util.UIUtil;

public class ExitAction implements ActionListener
{

	public void actionPerformed(ActionEvent event) 
	{
		Application.getInstance().shutdown();
	}
	public void handleQuit(ApplicationEvent event)
	{
		if (!Application.getInstance().shutdown())
			event.cancel();
	}
	/**
	 * Checks if the current version is up to date or not.
	 * If not (ie unsaved changes) then the user is prompted about saving work.
	 * 
	 * @return whether it is OK to unload the current project
	 * @throws MapperException 
	 */
	public static boolean checkForChangesAndPrompt() throws MapperException
	{
		Application app = Application.getInstance();
		Logger.getLogger(ExitAction.class).debug("ExitAction.checkForChangedAndPrompt; disk version="+app.getSaveVersion()+"; mem version="+DataRegistry.getHeadVersion());
		if (haveOpenEditors() || haveUnsavedChanged()) {
			IProject project = ProjectInputMapper.find();
			
			UnsavedChangesDialog.Result result = showUnsavedChangesDialog(project!=null?project.getName():"");
			
			switch (result) {
			case DONTSAVE:
				closeOpenEditors();
				return true;
			case CANCEL:
				return false;
			case SAVE:
				closeOpenEditors();
				new SaveProjectAction().performAction();
				// give exit sound a chance to play...
				try { Thread.sleep(1000); } catch (InterruptedException e) {}
				return true;
			}
		}
		return true;
	}
	private static UnsavedChangesDialog.Result showUnsavedChangesDialog(String projectName) {
		final UnsavedChangesDialog pane = new UnsavedChangesDialog(LanguageBundle.getString("confirmexitdialog.message", projectName));
		final JDialog dialog = new JDialog(Application.getInstance().getProjectExplorer());
		dialog.setBackground(dialog.getBackground().darker());
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.getContentPane().add(pane);
		dialog.setModal(true);
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		dialog.getRootPane().getActionMap().put("escape", new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				dialog.setVisible( false );
			}
		});
		dialog.getRootPane().setDefaultButton(pane.getCancelButton());
		dialog.pack();
		dialog.setSize(400, 150);
		UIUtil.centerOnFrame(dialog, Application.getInstance().getProjectExplorer());
		
		final StrongReference<UnsavedChangesDialog.Result> result = new StrongReference<UnsavedChangesDialog.Result>();
		pane.addResultActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				result.set(pane.getResult());
			}
		});
		
		dialog.setVisible(true);
		if ( result.isNull() )
			result.set( Result.CANCEL );
		return result.get();
	}
	private static boolean haveUnsavedChanged()
	{
		Application app = Application.getInstance();
		boolean haveChanges = app.getSaveVersion() != DataRegistry.getHeadVersion();
		return haveChanges;
	}
	private static boolean haveOpenEditors()
	{
		Application app = Application.getInstance();
		Collection<ResourceEditor> openEditors = app.getOpenEditors();
		return !openEditors.isEmpty();
	}
	private static void closeOpenEditors()
	{
		Application app = Application.getInstance();
		Collection<ResourceEditor> openEditors = app.getOpenEditors();
		for (ResourceEditor editor : openEditors) {
			UIUtil.closeWindow(editor);
		}
	}
}
