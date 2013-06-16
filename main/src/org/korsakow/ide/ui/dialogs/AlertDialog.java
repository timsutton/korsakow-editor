package org.korsakow.ide.ui.dialogs;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.korsakow.ide.util.UIHelper;

public class AlertDialog extends JPanel
{
	public static JComponent createMessage(String message)
	{
		JTextPane textField = new JTextPane();
		textField.setBackground(new JPanel().getBackground());
		textField.setEditable(false);
		textField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//		textField.setMaximumSize(new Dimension(640, 100));
		textField.setText(message);
		Dimension prefSize = new Dimension(400, 100);
		if (new JLabel(message).getPreferredSize().width < prefSize.width) {
			prefSize = textField.getPreferredSize();
		} else {
			prefSize.height += 30;
		}
		prefSize.height = Math.max(prefSize.height, 50);
		textField.setPreferredSize(prefSize);
		return textField;
	}
	private JCheckBox dontShowAgainCheck;
	private JComponent message;
	
	public AlertDialog(JComponent message)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(this.message = message);
		add(Box.createVerticalStrut(20));
		final JPanel panel = UIHelper.createHorizontalFlowLayoutPanel(dontShowAgainCheck = new JCheckBox("Do not show again"));
		add(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		dontShowAgainCheck.setVisible(false);
		
		setPreferredSize(new Dimension(640, 240));
		final Dimension maxSize = new Dimension(640, 480);
		setMaximumSize(maxSize);
		final Dimension prefSize = message.getPreferredSize();
		prefSize.width = Math.min(prefSize.width, maxSize.width);
		prefSize.height += dontShowAgainCheck.getPreferredSize().height;
		prefSize.height = Math.min(prefSize.height, maxSize.height);
		setPreferredSize(prefSize);
	}
	public boolean isOneTimeDialog() {
		return dontShowAgainCheck.isVisible();
	}
	public boolean getDontShowAgain() {
		return dontShowAgainCheck.isSelected();
	}
	public void setDontShowAgain(boolean dontshowagain) {
		dontShowAgainCheck.setSelected(dontshowagain);
	}
	public void setDontShowAgainVisible(boolean visible) {
		dontShowAgainCheck.setVisible(visible);
	}
}
