package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.snupool.SnuPool;
import org.korsakow.ide.ui.components.snupool.SnuPoolModel;
import org.korsakow.ide.ui.components.snupool.SnuPoolUpdateListener;

public class ShowSnuPoolWindowAction extends AbstractShowPoolWindowAction {
	
	public void actionPerformed(ActionEvent event) {
		Application app = Application.getInstance();
		if (app.getSnuPoolDialog() != null) {
			app.getSnuPoolDialog().toFront();
			return;
		}
		
		SnuPoolModel model;
		try {
			model = SnuPoolUpdateListener.createModel();
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			model = new SnuPoolModel();
		}
		
		SnuPool pool = new SnuPool();
		pool.setModel(model);
		
		org.korsakow.ide.ui.components.snupool.ContentUpdater updater = new org.korsakow.ide.ui.components.snupool.ContentUpdater(pool);
		pool.addActionListener(updater);
		pool.addListSelectionListener(updater);
		
		SnuPoolUpdateListener updateListener = new SnuPoolUpdateListener(pool);
		Application.getInstance().addApplicationListener(updateListener);
		pool.putClientProperty("applicationListener", updateListener); // tie lifetime to pool since applisteners are weak-refs
		JFrame dialog = createPoolDialog(LanguageBundle.getString("snupool.window.title"), pool);
		app.setSnuPoolDialog(dialog, pool);
		dialog.setLocation(app.getProjectExplorer().getX()+200, app.getProjectExplorer().getY()-dialog.getInsets().top);
		dialog.setVisible(true);
	}

}
