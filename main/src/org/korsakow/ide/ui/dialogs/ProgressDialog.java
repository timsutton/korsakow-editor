package org.korsakow.ide.ui.dialogs;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.korsakow.ide.util.UIHelper;
import org.korsakow.ide.util.UIUtil;

public class ProgressDialog extends JPanel
{
	public static void main(String[] args)
	{
		UIUtil.setUpLAF();
		JDialog dialog = new JDialog();
		dialog.add(new ProgressDialog());
		dialog.pack();
		dialog.setVisible(true);
	}
	
	private JProgressBar mainProgressBar;
	private JProgressBar subProgressBar;
	public ProgressDialog()
	{
		initUI();
	}
	
	private void initUI()
	{
		mainProgressBar = new JProgressBar(0, 100);
		mainProgressBar.setStringPainted(true);
		mainProgressBar.setValue(0);
		subProgressBar = new JProgressBar(0, 100);
		subProgressBar.setStringPainted(true);
		subProgressBar.setValue(0);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
		
		add(UIHelper.createHorizontalMarginPanel(new JLabel("Task", JLabel.LEFT), 0, 0));
		add(UIHelper.createHorizontalMarginPanel(mainProgressBar, 0, 0));
//		mainProgressBar.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(Box.createVerticalStrut(5));
		add(UIHelper.createHorizontalMarginPanel(new JLabel("Progress", JLabel.LEFT), 0, 0));
		add(UIHelper.createHorizontalMarginPanel(subProgressBar, 0, 0));
//		subProgressBar.setMaximumSize(new Dimension(Short.MAX_VALUE, 15));
//		subProgressBar.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		setSize(640, 480);
	}
	
	public void setMainProgress(int value)
	{
		mainProgressBar.setValue(value);
	}
	public void setMainDisplayString(String string)
	{
		mainProgressBar.setString(string);
	}
	public void setSubProgress(int value)
	{
		subProgressBar.setValue(value);
	}
	public void setSubDisplayString(String string)
	{
		subProgressBar.setString(string);
	}
	public JProgressBar getMainProgressBar()
	{
		return mainProgressBar;
	}
	public JProgressBar getSubProgressBar()
	{
		return subProgressBar;
	}
}
