package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.PossiblePoolApplicationListener;
import org.korsakow.ide.ui.controller.PossiblePoolMouseListener;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableHeaderCellRenderer;
import org.korsakow.ide.ui.resources.UnusedMediaResourceTreeTableModel;
import org.korsakow.ide.util.UIHelper;

public class ShowPossiblePoolWindowAction extends AbstractShowPoolWindowAction {

	public void actionPerformed(ActionEvent e) {
		Application app = Application.getInstance();
		if (app.getPossiblePoolDialog() != null) {
			app.getPossiblePoolDialog().toFront();
			return;
		}
		
		ResourceTreeTable treeTable = new ResourceTreeTable();
		treeTable.setAutoCreateColumnsFromModel(false);
		final UnusedMediaResourceTreeTableModel unusedVideoModel = new UnusedMediaResourceTreeTableModel(treeTable, app.getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel(), false, true);
		treeTable.getTableHeader().setDefaultRenderer(new ResourceTreeTableHeaderCellRenderer());
		treeTable.setTreeTableModel(unusedVideoModel);
		treeTable.addMouseListener(new PossiblePoolMouseListener());
		
		JCheckBox imageFilter, videoFilter;

		JPanel content = UIHelper.createVerticalBoxLayoutPanel(
				UIHelper.createHorizontalBoxLayoutPanel(
						imageFilter = new JCheckBox("Images"),
						UIHelper.createHorizontalStrut(10),
						videoFilter = new JCheckBox("Videos")
				),
				new JScrollPane(treeTable)
				);
		
		imageFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				unusedVideoModel.setIncludeImages(((JCheckBox)event.getSource()).isSelected());
				unusedVideoModel.update();
			}
		});
		videoFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				unusedVideoModel.setIncludeVideos(((JCheckBox)event.getSource()).isSelected());
				unusedVideoModel.update();
			}
		});
		
		videoFilter.setSelected(true);
		
		PossiblePoolApplicationListener updateListener = new PossiblePoolApplicationListener(treeTable);
//		app.getProjectExplorer().getResourceExplorer().getResourceBrowser().getResourceTreeTable().getModel().addTableModelListener(updateListener);
		app.getProjectExplorer().getResourceBrowser().getResourceTreeTable().addPropertyChangeListener("model", updateListener);
		Application.getInstance().addApplicationListener(updateListener);
		treeTable.putClientProperty("applicationListener", updateListener); // tie lifetime to pool since applisteners are weak-refs
		JFrame dialog = createPoolDialog(LanguageBundle.getString("possiblepool.window.title"), content);
		app.setPossiblePoolDialog(dialog, treeTable);
		dialog.setSize(300, dialog.getHeight());
		dialog.setLocation(app.getProjectExplorer().getX()+600, app.getProjectExplorer().getY()-dialog.getInsets().top);
		dialog.setVisible(true);
	}

}
