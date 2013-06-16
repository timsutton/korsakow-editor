package org.korsakow.ide.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import org.korsakow.ide.ui.components.layout.VerticalFlowLayout;

public class K3ImportDialog extends JPanel
{
	private JLabel fileLabel;
	private JLabel lineLabel;
	private JTextArea messageLabel;
	public K3ImportDialog(String filename, String message)
	{
		this(filename, null, message);
	}
	public K3ImportDialog(String filename, Integer line, String message)
	{
		initUI();
		fileLabel.setText(filename);
		fileLabel.setVisible(fileLabel.getText().length()>0);
		if (line != null)
			lineLabel.setText(""+line);
		lineLabel.setVisible(lineLabel.getText().length()>0);
		messageLabel.setText(message);
		messageLabel.setVisible(messageLabel.getText().length()>0);
		setPreferredSize(new Dimension(640, 240));
	}
	private void initUI()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel panel;
		add(Box.createVerticalStrut(5));
		add(createPanel(new JLabel("File: "), fileLabel = new JLabel()));
		add(Box.createVerticalStrut(2));
		add(createPanel(new JLabel("Line: "), lineLabel = new JLabel()));
		add(Box.createVerticalStrut(10));
		add(panel = createPanel(new JLabel("Message"), null));
		add(messageLabel = new JTextArea());

		messageLabel.setEditable(false);
		messageLabel.setWrapStyleWord(true);
		messageLabel.setLineWrap(true);
		messageLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	private JPanel createPanel(JComponent left, JComponent right)
	{
		JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
		panel.add(left, BorderLayout.WEST);
		if (right != null)
			panel.add(right, BorderLayout.CENTER);
		return panel;
	}
}
