package org.korsakow.ide.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.EventListenerList;

import org.korsakow.ide.util.UIUtil;

public class UnsavedChangesDialog extends JPanel
{
	public static enum Result {
		DONTSAVE,
		CANCEL,
		SAVE,
	}
	
	private JTextArea messageLabel;
	private final EventListenerList listeners = new EventListenerList();
	private JButton dontSaveButton;
	private JButton cancelButton;
	private JButton saveButton;
	private Result result;
	
	public UnsavedChangesDialog(String message)
	{
//		setBackground(new Color(0.83f, 0.83f, 0.83f));
		setBackground(new Color(0.79f, 0.79f, 0.79f));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.setOpaque(true);
		messagePanel.setBackground(new Color(0.89f, 0.89f, 0.89f));
		messagePanel.add(messageLabel = new JTextArea(message));
		add(messagePanel);
		messagePanel.setAlignmentX(RIGHT_ALIGNMENT);
		messageLabel.setLineWrap(true);
		messageLabel.setOpaque(false);
		messageLabel.setWrapStyleWord(true);
		messageLabel.setMinimumSize(messageLabel.getPreferredSize());
		messageLabel.setFocusable( false );
//		messageLabel.setVerticalAlignment(SwingConstants.TOP);
//		messageLabel.setVerticalTextPosition(SwingConstants.TOP);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.add(dontSaveButton = new JButton("Don't Save"));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancelButton = new JButton("Cancel"));
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(saveButton = new JButton("Save"));
		
		buttonPanel.setAlignmentX(RIGHT_ALIGNMENT);
		add(Box.createVerticalStrut(10));
		add(buttonPanel);
		
//		final Dimension maxSize = new Dimension(640, 480);
//		setMaximumSize(maxSize);
//		final Dimension prefSize = message.getPreferredSize();
//		prefSize.width = Math.min(prefSize.width, maxSize.width);
//		prefSize.height += dontShowAgainCheck.getPreferredSize().height;
//		prefSize.height = Math.min(prefSize.height, maxSize.height);
//		setPreferredSize(prefSize);
		
		
		dontSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				notifyResult(Result.DONTSAVE);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				notifyResult(Result.CANCEL);
			}
		});
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				notifyResult(result = Result.SAVE);
			}
		});
		
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				cancelButton.requestDefaultFocus();
				cancelButton.requestFocus();
			}
		});
	}
	
	private void notifyResult(Result result) {
		this.result = result;
		ActionEvent event = new ActionEvent(this, 0, "result");
		for (ActionListener listener : listeners.getListeners(ActionListener.class))
			listener.actionPerformed(event);
	}
	public JButton getCancelButton() {
		return cancelButton;
	}
	public void addResultActionListener(ActionListener listener) {
		listeners.add(ActionListener.class, listener);
	}
	public Result getResult() {
		return result;
	}
}
