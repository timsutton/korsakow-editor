package org.korsakow.ide.ui.resources;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;

public class MediaResourceView extends ResourceView
{
	protected JPanel mediaPanel;
	protected JLabel filenameLabel;
	protected JTextField filenameField;
	protected JButton filenameButton;
	
	public MediaResourceView()
	{
	}
		@Override
		protected void initUI()
		{
			super.initUI();
		mediaPanel = new JPanel();
		mediaPanel.setLayout(new BoxLayout(mediaPanel, BoxLayout.Y_AXIS));
		mainPanel.add(mediaPanel);
//		Box box = Box.createHorizontalBox();
		JPanel filenamePanel = new JPanel(new BorderLayout());
		mediaPanel.add(filenamePanel);
		filenamePanel.add(filenameLabel = new JLabel(LanguageBundle.getString("mediaresourceview.filename.label")), BorderLayout.WEST);
		filenamePanel.add(filenameField = new JTextField());
		filenamePanel.add(filenameButton = new JButton("..."), BorderLayout.EAST);
		filenamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, filenameField.getPreferredSize().height)); // why is this necesasry?
	}
	@Override
	protected void initListeners()
	{
		super.initListeners();
		filenameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File defaultFile = new File(filenameButton.getText());
				File file = Application.getInstance().showFileOpenDialog(MediaResourceView.this, defaultFile);
				if (file != null)
					filenameField.setText(file.getAbsolutePath());
			}
		});
	}
	public void setFilenameVisible(boolean visible)
	{
		filenameLabel.setVisible(visible);
		filenameField.setVisible(visible);
	}
	public void setFilename(String filename)
	{
		filenameField.setText(filename);
	}
	public String getFilename()
	{
		return filenameField.getText();
	}
	@Override
	public void dispose()
	{
		super.dispose();
	}
}
