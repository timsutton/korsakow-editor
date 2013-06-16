package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.linkpool.LinkPool;
import org.korsakow.ide.ui.components.linkpool.LinkPoolModel;
import org.korsakow.ide.ui.components.linkpool.LinkPoolUpdateListener;
import org.korsakow.ide.ui.components.linkpool.SnuHeaderEntry;

public class ShowLinkPoolWindowAction extends AbstractShowPoolWindowAction {

	public void actionPerformed(ActionEvent e) {
		Application app = Application.getInstance();
		if (app.getLinkPoolDialog() != null) {
			app.getLinkPoolDialog().toFront();
			return;
		}
		
		Collection<ISnu> snus;
		try {
			snus = SnuInputMapper.findAll();
		} catch (MapperException e1) {
			Application.getInstance().showUnhandledErrorDialog(e1);
			snus = Collections.EMPTY_LIST;
		}
		
		LinkPoolModel model = new LinkPoolModel();
		model.beginBatchUpdate();
		for (ISnu snu : snus)
		{
			model.add(new SnuHeaderEntry(snu.getId(), snu.getName()));
		}
		model.endBatchUpdate();
		
		LinkPool pool = new LinkPool();
		pool.setModel(model);
		
		org.korsakow.ide.ui.components.linkpool.ContentUpdater updater = new org.korsakow.ide.ui.components.linkpool.ContentUpdater(pool);
		pool.addActionListener(updater);
		pool.addListSelectionListener(updater);
		
		LinkPoolUpdateListener updateListener = new LinkPoolUpdateListener(pool);
		Application.getInstance().addApplicationListener(updateListener);
		pool.putClientProperty("applicationListener", updateListener); // tie lifetime to pool since applisteners are weak-refs
		JFrame dialog = createPoolDialog(LanguageBundle.getString("linkpool.window.title"), pool);
		app.setLinkPoolDialog(dialog, pool);
		dialog.setLocation(app.getProjectExplorer().getX()+400, app.getProjectExplorer().getY()-dialog.getInsets().top);
		dialog.setVisible(true);
	}

}
