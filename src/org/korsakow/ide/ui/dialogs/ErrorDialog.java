package org.korsakow.ide.ui.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ErrorDialog extends JPanel
{
	private static JComponent createMessage(String message)
	{
		JTextArea textField = new JTextArea();
		textField.setEditable(false);
		textField.setBorder(null);
		textField.setWrapStyleWord(true);
//		textField.setMaximumSize(new Dimension(640, 100));
		textField.setText(message);
//		return textField;
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(640, 100));
		return scrollPane;
	}
	private static JComponent createDetails(String message)
	{
		if (message == null)
			return null;
		JTextArea textField = new JTextArea();
		textField.setEditable(false);
		textField.setWrapStyleWord(true);
		textField.setBackground(new JLabel().getBackground());
		textField.setText(message);
//		return textField;
		JScrollPane scrollPane = new JScrollPane(textField);
		scrollPane.setPreferredSize(new Dimension(640, 380));
		return scrollPane;
	}
	private JComponent message;
	private JComponent details;
	
	public ErrorDialog(String message)
	{
		this(message, null);
	}
	public ErrorDialog(String message, String details)
	{
		this(createMessage(message), createDetails((details)));
//		((JTextArea)this.details).setEditable(false);
	}
	public ErrorDialog(JComponent message, JComponent details)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(this.message = message);
		add(Box.createVerticalStrut(20));
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		if (details != null) {
			JLabel label = new JLabel("Error Details", JLabel.LEFT);
			label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			panel.add(label);
		}
		add(panel);
		if (details != null)
			add(this.details = details);
		
//		setPreferredSize(new Dimension(640, 240));
		final Dimension maxSize = new Dimension(640, details!=null?480:120);
		setMaximumSize(maxSize);
		setPreferredSize(maxSize);
	}
}
