package org.korsakow.ide.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;

public class EditingConflictDialog extends JDialog
{
	protected ResourceTreeTable tree;
	protected ResourceTreeTableModel treeModel;
	protected JTextArea messageLabel = new JTextArea();
	protected JButton cancelButton = new JButton(LanguageBundle.getString("general.cancelbutton.label"));
	protected JButton breakLinksButton = new JButton(LanguageBundle.getString("general.breaklinksbutton.label"));
	public EditingConflictDialog(JFrame parent)
	{
		super(parent);
		initUI();
		initListeners();
	}
	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(true);
	}
	public void setMessage(String msg)
	{
		messageLabel.setText(msg);
	}
	public void addConflictItem(IResource item, Collection<IResource> references)
	{
		KNode itemNode = ResourceNode.create(item);
		if (references != null)
			for (IResource ref : references)
			{
				KNode refNode = ResourceNode.create(ref);
				itemNode.add(refNode);
			}
		treeModel.appendNode(itemNode, treeModel.getRoot());
		tree.expandNode(tree.getRootNode());
		tree.expandAllRecursive();
	}
	public ResourceTreeTableModel getModel()
	{
		return treeModel;
	}
	protected void initUI()
	{
//		setLayout(new BoxLayout());
		Box mainPanel = Box.createVerticalBox();
		JPanel panel;
		Box box;
		add(mainPanel);
		
		//panel = new JPanel();
		//panel.setLayout(new BorderLayout());
		mainPanel.add(messageLabel);
		//panel.add(messageLabel);
		messageLabel.setLineWrap(true);
		messageLabel.setWrapStyleWord(true);
		messageLabel.setEditable(false);
		messageLabel.setBackground(new JLabel().getBackground());
		messageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, messageLabel.getPreferredSize().height));
		
		mainPanel.add(Box.createVerticalGlue());
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new TitledBorder("Conflicts"));
		mainPanel.add(panel);
		
		tree = new ResourceTreeTable();
		treeModel = tree.getTreeTableModel();
		tree.setRootVisible(false);
		panel.add(new JScrollPane(tree));
		
		mainPanel.add(Box.createVerticalGlue());
		box = Box.createHorizontalBox();
		mainPanel.add(box);
		box.add(Box.createHorizontalGlue());
		box.add(breakLinksButton);
		box.add(Box.createHorizontalStrut(20));
		box.add(cancelButton);
		box.add(Box.createHorizontalStrut(10));
	}
	protected void initListeners()
	{
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onCloseAction();
			}
		});
	}
	protected void onCloseAction()
	{
		dispose();
	}
	public void setBreakLinksAction(ActionListener actionListener)
	{
		breakLinksButton.removeActionListener(actionListener);
		breakLinksButton.addActionListener(actionListener);
	}
}
