package org.korsakow.ide.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.MediaProperty;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeCellRenderer;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.UIUtil;

public class MissingMediaDialog extends JDialog
{
	private static class FileNode extends KNode
	{
		private final String cachedName;
		public FileNode(String path)
		{
			this(new File(path));
		}
		public FileNode(File file) {
			super(file.getAbsolutePath());
			cachedName = file.getName();
		}
		public String getFilename()
		{
			return cachedName;
		}
	}
	private static class MyTreeCellRenderer extends ResourceTreeCellRenderer
	{
		@Override
		public Component getTreeCellRendererComponent(JTree tree, KNode value,
				boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus)
		{
			super.getTreeCellRendererComponent(tree, value.getName(), selected, expanded, leaf, row, hasFocus);
			if (value instanceof FileNode) {
				FileNode fileNode = (FileNode)value;
				setText(fileNode.getFilename());
			}
			return this;
		}
	}
	private static class MyTreeTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			if (value instanceof FileNode) {
				setText(((FileNode)value).getName());
			} else {
				setText("");
			}
			return this;
		}
	}
	private final Collection<IMedia> mediaCache = new HashSet<IMedia>();
	private ResourceTreeTable tree;
	private ResourceTreeTableModel treeModel;
	private final JTextArea messageLabel = new JTextArea();
	private final JButton findMissingButton = new JButton(LanguageBundle.getString("missingmediadialog.findmissing.label"));
	private final JButton closeButton = new JButton(LanguageBundle.getString("missingmediadialog.close.label"));
	public MissingMediaDialog(JFrame parent)
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
	public void addMissingItem(IMedia item)
	{
		addMissingItem(item, Collections.EMPTY_LIST);
	}
	public void setPossibleMatches(IMedia item, Collection<File> possibleMatches)
	{
		KNode itemNode = treeModel.findResource(item.getId());
		for (File match : possibleMatches)
		{
			KNode fileNode = new FileNode(match.getAbsolutePath());
			itemNode.add(fileNode);
		}
	}
	public void addMissingItem(IMedia item, Collection<File> possibleMatches)
	{
		KNode itemNode = ResourceNode.create(item);
		setPossibleMatches(item, possibleMatches);
		treeModel.appendNode(itemNode, treeModel.getRoot());
		mediaCache.add(item);
	}
	public void removeMissingItem(IMedia item)
	{
		treeModel.remove(item.getId());
		mediaCache.remove(item);
	}
	public Collection<IMedia> getMissingMedia()
	{
		return new HashSet<IMedia>(mediaCache);
	}
	public void expandAll()
	{
		tree.expandNode(tree.getRootNode());
		tree.expandAllRecursive();
	}
	private void initUI()
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
		messageLabel.setEditable(false);
		messageLabel.setBackground(new JLabel().getBackground());
		messageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, messageLabel.getPreferredSize().height));
		
		mainPanel.add(Box.createVerticalGlue());
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new TitledBorder(LanguageBundle.getString("missingmediadialog.missing.label")));
		mainPanel.add(panel);
		
		tree = new ResourceTreeTable();
		tree.setTreeCellRenderer(new MyTreeCellRenderer());
		tree.setDefaultRenderer(Object.class, new MyTreeTableCellRenderer());
		treeModel = tree.getTreeTableModel();
		tree.setRootVisible(false);
		panel.add(new JScrollPane(tree));
		UIUtil.addColumn(tree, LanguageBundle.getString("resourcebrowser.columns.filename.label"), MediaProperty.FILENAME);
		
		
		mainPanel.add(Box.createVerticalGlue());
		box = Box.createHorizontalBox();
		mainPanel.add(box);
		box.add(Box.createHorizontalGlue());
		box.add(findMissingButton);
		box.add(Box.createHorizontalStrut(20));
		box.add(closeButton);
		box.add(Box.createHorizontalStrut(5));
		getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		getRootPane().getActionMap().put("escape", new AbstractAction()
		{
			public void actionPerformed(ActionEvent event) {
				closeButton.doClick();
			}
		});
	}
	private void initListeners()
	{
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onCloseAction();
			}
		});
	}
	public void setFindMissingAction(ActionListener action)
	{
		findMissingButton.addActionListener(action);
	}
	private void onCloseAction()
	{
		dispose();
	}
}
