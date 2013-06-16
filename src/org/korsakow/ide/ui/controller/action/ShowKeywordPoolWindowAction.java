package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.CountSnuByInKeywordCommand;
import org.korsakow.domain.command.CountSnuByOutKeywordCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.input.KeywordInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.keywordpool.KeywordPool;
import org.korsakow.ide.ui.components.keywordpool.KeywordPoolInOutListener;
import org.korsakow.ide.ui.components.keywordpool.KeywordPoolModel;
import org.korsakow.ide.ui.components.keywordpool.KeywordPoolUpdateListener;
import org.korsakow.services.tdg.SnuTDG;

public class ShowKeywordPoolWindowAction extends AbstractShowPoolWindowAction {

	public void actionPerformed(ActionEvent event) {
		Application app = Application.getInstance();
		if (app.getKeywordPoolDialog() != null) {
			app.getKeywordPoolDialog().toFront();
			return;
		}
		KeywordPool pool = new KeywordPool();
		
		ActionListener inoutListener = new KeywordPoolInOutListener(pool);
		
		pool.addInActionListener(inoutListener);
		pool.addOutActionListener(inoutListener);
		pool.addItemActionListener(inoutListener);
		Collection<IKeyword> keywords;
		try {
			keywords = KeywordInputMapper.findByObjectTypeRecursive( SnuTDG.NODE_NAME );
		} catch (MapperException e) {
			keywords = new ArrayList<IKeyword>();
			Application.getInstance().showUnhandledErrorDialog( e );
		}
		
		KeywordPoolModel model = new KeywordPoolModel();
		model.beginBatchUpdate();
		try {
			for (IKeyword keyword : keywords)
			{
				int inCount = CommandExecutor.executeCommand(CountSnuByInKeywordCommand.class, Request.single("keyword", keyword.getValue())).getInt("count");
				int outCount = CommandExecutor.executeCommand(CountSnuByOutKeywordCommand.class, Request.single("keyword", keyword.getValue())).getInt("count");
				model.add(keyword, inCount, outCount);
			}
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
		}
		model.endBatchUpdate();
		pool.setModel(model);
		
		org.korsakow.ide.ui.components.keywordpool.ContentUpdater updater = new org.korsakow.ide.ui.components.keywordpool.ContentUpdater(pool);
		pool.addActionListener(updater);
		pool.addListSelectionListener(updater);
		
		KeywordPoolUpdateListener updateListener = new KeywordPoolUpdateListener(pool);
		Application.getInstance().addApplicationListener(updateListener);
		pool.putClientProperty("applicationListener", updateListener); // tie lifetime to pool since applisteners are weak-refs
		
		JFrame dialog = createPoolDialog(LanguageBundle.getString("keywordpool.window.title"), pool);
		app.setKeywordPoolDialog(dialog, pool);
		dialog.setLocation(app.getProjectExplorer().getX(), app.getProjectExplorer().getY()-dialog.getInsets().top);
		dialog.setVisible(true);
	}

}
