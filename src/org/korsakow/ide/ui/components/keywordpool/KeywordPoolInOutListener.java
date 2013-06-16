package org.korsakow.ide.ui.components.keywordpool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JComponent;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.Application;
import org.korsakow.ide.code.k5.K5Code;
import org.korsakow.ide.code.k5.K5Symbol;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.components.code.CodeTableModel;
import org.korsakow.ide.ui.resources.ResourceView;
import org.korsakow.ide.ui.resources.SnuResourceView;

public class KeywordPoolInOutListener implements ActionListener
{
	KeywordPool pool;
	public KeywordPoolInOutListener(KeywordPool pool)
	{
		this.pool = pool;
	}
	public void actionPerformed(ActionEvent event)
	{
		Object id = ((JComponent)event.getSource()).getClientProperty("poolid");
		if (event.getActionCommand() == KeywordHeader.IN_ACTION)
			inAction(id);
		else
		if (event.getActionCommand() == KeywordHeader.OUT_ACTION)
			outAction(id);
		else
		if (event.getActionCommand() == KeywordHeader.ITEM_ACTION)
			itemAction(id);
			
	}
	/**
	 * Adds the keyword as an in keyword
	 * @param id
	 */
	private void inAction(Object id)
	{
		IKeyword keyword = pool.getModel().getEntry(id).getKeyword();
		Application app = Application.getInstance();
		ResourceEditor editor = app.getMostRecentFocusedEditor();
		if (editor == null)
			return;
		ResourceView resourceView = editor.getResourceView();
		if (resourceView instanceof SnuResourceView) {
			SnuResourceView snuView = (SnuResourceView)resourceView;
			Collection<IKeyword> keywords = snuView.getKeywords();
			if (keywords.contains(keyword))
				keywords.remove(keyword);
			else
				keywords.add(keyword);
			snuView.setKeywords(keywords);
			editor.toFront();
		}
	}
	/**
	 * Adds the keyword as an out keyword
	 * @param id
	 */
	private void outAction(Object id)
	{
		IKeyword keyword = pool.getModel().getEntry(id).getKeyword();
		Application app = Application.getInstance();
		ResourceEditor editor = app.getMostRecentFocusedEditor();
		if (editor == null)
			return;
		ResourceView resourceView = editor.getResourceView();
		if (resourceView instanceof SnuResourceView) {
			SnuResourceView snuView = (SnuResourceView)resourceView;
			if (snuView.getCodeTable().getRowCount() == 0)
				return;
			int selectedRow = snuView.getCodeTable().getSelectedRow();
			if (selectedRow == -1)
				selectedRow = 0;
			if (selectedRow != -1)
			{
				CodeTableModel model = snuView.getCodeTable().getModel();
				K5Code code = model.getCodeAt(selectedRow);
				String raw = code.getRawCode();
				raw += K5Symbol.DEFAULT_STATEMENT_SEPARATOR_STRING + keyword.getValue();
				code = new K5Code(raw);
				model.setCodeAt(code, selectedRow);
				editor.toFront();
			}
		}
	}
	private void itemAction(Object id)
	{
		inAction(id);
		outAction(id);
	}
}
