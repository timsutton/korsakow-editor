package org.korsakow.ide.ui.resources;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.korsakow.ide.Application;

public class SubtitledMediaResourceView extends MediaResourceView
{

	protected JLabel subtitleLabel;
	protected JTextField subtitleField;
	protected JButton subtitleButton;

	public SubtitledMediaResourceView()
	{
		super();
	}

	@Override
	protected void initUI()
	{
		super.initUI();
		JPanel subtitlePanel = new JPanel(new BorderLayout());
		mediaPanel.add(subtitlePanel);
		subtitlePanel.add(subtitleLabel = new JLabel("Subtitles"), BorderLayout.WEST);
		subtitlePanel.add(subtitleField = new JTextField());
		subtitlePanel.add(subtitleButton = new JButton("..."), BorderLayout.EAST);
		subtitlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, filenameField.getPreferredSize().height)); // why is this necesasry?
	}

	@Override
	protected void initListeners()
	{
		super.initListeners();
		
		subtitleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File defaultFile = new File(subtitleField.getText());
				File file = Application.getInstance().showFileOpenDialog(SubtitledMediaResourceView.this, defaultFile);
				if (file != null)
					subtitleField.setText(file.getAbsolutePath());
			}
		});
	}

	public void setSubtitles(String subtitles)
	{
		subtitleField.setText(subtitles);
	}
	public String getSubtitles()
	{
		return subtitleField.getText();
	}

}