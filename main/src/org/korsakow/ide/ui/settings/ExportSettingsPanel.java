/**
 * 
 */
package org.korsakow.ide.ui.settings;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.model.KComboboxModel;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.UIHelper;
import org.korsakow.services.export.IVideoEncodingProfile;

public class ExportSettingsPanel extends JPanel implements ISettingsPanel
{
	private JCheckBox exportVideosCheck;
	private JCheckBox exportSoundsCheck;
	private JCheckBox exportSubtitlesCheck;
	private JCheckBox exportImagesCheck;
	private JCheckBox exportWebFilesCheck;
	private JTextField exportDirectory;
	private JButton exportDirectoryButton;

	private JComboBox videoEncodingProfileCombo;
	private JCheckBox encodeVideoOnExportCheck;
	
	public ExportSettingsPanel()
	{
		initUI();
		initListeners();
	}
	private void initUI()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel panel;
		add(panel = createExportTypesPanel());
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(panel = createVideoPanel());
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(panel = createFolderPanel());
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		add(Box.createVerticalGlue());
	}
	private JPanel createExportTypesPanel() {
		JPanel content = new JPanel();
		content.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		
		content.add(new JLabel("Types to export:"));

		content.add( Box.createVerticalStrut(5) );
		
		Box checkPanel = Box.createVerticalBox();
		checkPanel.setAlignmentX(LEFT_ALIGNMENT);
		checkPanel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
		checkPanel.add( exportVideosCheck = new JCheckBox("Videos") );
		checkPanel.add( Box.createVerticalStrut(5) );
		checkPanel.add( exportImagesCheck = new JCheckBox("Images") );
		checkPanel.add( Box.createVerticalStrut(5) );
		checkPanel.add( exportSoundsCheck = new JCheckBox("Sounds") );
		checkPanel.add( Box.createVerticalStrut(5) );
		checkPanel.add( exportSubtitlesCheck = new JCheckBox("Subtitles") );
		checkPanel.add( Box.createVerticalStrut(5) );
		checkPanel.add( exportWebFilesCheck = new JCheckBox("Web Files") );
		content.add(checkPanel);

		content.add( Box.createVerticalStrut(15) );
		
		content.add(new JLabel("Export location"));
		Box exportPanel = Box.createHorizontalBox();
		exportPanel.setAlignmentX(LEFT_ALIGNMENT);
		exportPanel.add(exportDirectory = new JTextField());
		exportDirectory.setMinimumSize(new Dimension(200, 25));
		exportDirectory.setMaximumSize(new Dimension(2000, 25));
		exportPanel.add(exportDirectoryButton = new JButton("..."));
		content.add(exportPanel);
		
		content.add( Box.createVerticalStrut(10) );
		
		return content;
	}
	private JPanel createVideoPanel()
	{
		JPanel videoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		videoPanel.setLayout(new BoxLayout(videoPanel, BoxLayout.Y_AXIS));
		videoPanel.setBorder(BorderFactory.createTitledBorder("Video"));
		videoPanel.add(UIHelper.createLabelPanel("", LanguageBundle.getString("projectsettings.encodeonexport.label"), encodeVideoOnExportCheck = UIFactory.getFactory().createCheckBox("")));
		videoPanel.add(UIHelper.createLabelPanel("", "Encoding profile", videoEncodingProfileCombo = UIFactory.getFactory().createComboBox("", new DefaultComboBoxModel(), new VideoEncodingProfileRenderer())));
		return videoPanel;
	}
	private JPanel createFolderPanel()
	{
		IUIFactory fac = UIFactory.getFactory();
		JPanel folderPanel = new JPanel();
//			Util.list(Component.class, fac.createLabel("Publish"), fac.createTextField("")),
//			Util.list(Component.class, fac.createLabel("Draft"), fac.createTextField(""))
//		);
		
//		folderPanel.setBorder(BorderFactory.createTitledBorder("Folders"));
//		folderPanel.setLayout(new BoxLayout(folderPanel, BoxLayout.Y_AXIS));
//		folderPanel.add(UIHelper.createBorderLayoutLabelPanel("Publish Folder", publishFeld = UIFactory.getFactory().createTextField("")));
//		folderPanel.add(UIHelper.createBorderLayoutLabelPanel("Draft Folder", draftField = UIFactory.getFactory().createTextField("")));
		return folderPanel;
	}
	private void initListeners()
	{
		encodeVideoOnExportCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!encodeVideoOnExportCheck.isSelected())
					Application.getInstance().showOneTimeAlertDialog("EncodeOnExport", ExportSettingsPanel.this, LanguageBundle.getString("projectsettings.encodeonexport.warning.title"), LanguageBundle.getString("projectsettings.encodeonexport.warning.message"));
				videoEncodingProfileCombo.setEnabled(encodeVideoOnExportCheck.isSelected());
			}
		});
		
		exportDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String currentValue = exportDirectory.getText().trim();
				if (currentValue.isEmpty())
					currentValue = "index.html";
				File file = Application.getInstance().showFileSaveDialog(exportDirectoryButton, new File(currentValue));
				if (file != null) {
					if (FileUtil.isProbablyADirectory(file))
						file = new File(file, "index.html");
					exportDirectory.setText(file.getPath());
				}
			}
		});
		
		class Test implements ActionListener
		{
			public void actionPerformed(ActionEvent event ) {
				final JCheckBox source = (JCheckBox)event.getSource();
				if (!source.isSelected())
					Application.getInstance().showOneTimeAlertDialog("ExportSettings.exportTypes", source, "Reminder", "Please remember to do a full export at least once!");
			}
		}
		exportVideosCheck.addActionListener(new Test());
		exportImagesCheck.addActionListener(new Test());
		exportSoundsCheck.addActionListener(new Test());
		exportSubtitlesCheck.addActionListener(new Test());
		exportWebFilesCheck.addActionListener(new Test());
	}
	public void setVideoEncodingProfileChoices(List<IVideoEncodingProfile> choices)
	{
		videoEncodingProfileCombo.setModel(new KComboboxModel(choices));
	}
	public void setVideoEncodingProfile(IVideoEncodingProfile profile)
	{
		videoEncodingProfileCombo.setSelectedItem(profile);
	}
	public IVideoEncodingProfile getVideoEncodingProfile()
	{
		return (IVideoEncodingProfile)videoEncodingProfileCombo.getSelectedItem();
	}
	public void setEncodeVideoOnExport(boolean encode) {
		encodeVideoOnExportCheck.setSelected(encode);
		videoEncodingProfileCombo.setEnabled(encodeVideoOnExportCheck.isSelected());
	}
	public boolean getEncodeVideoOnExport() {
		return encodeVideoOnExportCheck.isSelected();
	}
	
	public boolean getExportVideos() {
		return exportVideosCheck.isSelected();
	}
	public void setExportVideos(boolean video) {
		exportVideosCheck.setSelected(video);
	}
	public boolean getExportImages() {
		return exportImagesCheck.isSelected();
	}
	public void setExportImages(boolean image) {
		exportImagesCheck.setSelected(image);
	}
	public boolean getExportSounds() {
		return exportSoundsCheck.isSelected();
	}
	public void setExportSounds(boolean sound) {
		exportSoundsCheck.setSelected(sound);
	}
	public boolean getExportSubtitles() {
		return exportSubtitlesCheck.isSelected();
	}
	public void setExportSubtitles(boolean subtitle) {
		exportSubtitlesCheck.setSelected(subtitle);
	}
	public boolean getExportWebFiles() {
		return exportWebFilesCheck.isSelected();
	}
	public void setExportWebFiles(boolean web) {
		exportWebFilesCheck.setSelected(web);
	}
	
	public String getExportDirectory() {
		return exportDirectory.getText();
	}
	public void setExportDirectory(String dir) {
		exportDirectory.setText(dir);
	}
	
	public void dispose()
	{
	}
	private static class VideoEncodingProfileRenderer extends DefaultListCellRenderer
	{
	    @Override
		public Component getListCellRendererComponent(
	            JList list,
	    	Object value,
	            int index,
	            boolean isSelected,
	            boolean cellHasFocus)
	    {
	    	super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    	if (value != null)
	    		setText(((IVideoEncodingProfile)value).getName());
	    	return this;
	    }
	}
}
