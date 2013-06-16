/**
 * 
 */
package org.korsakow.ide.ui.settings;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.cell.ResourceDOComboBoxRenderer;
import org.korsakow.ide.ui.components.model.ResourceComboBoxModel;
import org.korsakow.ide.ui.constraints.NumberRangeTextFieldConstraint;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.ui.resources.SnuResourceView;
import org.korsakow.ide.util.UIResourceManager;

public class MovieSettingsPanel extends JPanel implements ISettingsPanel
{
	public static enum Action
	{
		BackgroundSoundActivated,
		AddBackgroundSound,
		EditBackgroundSound,
		DeleteBackgroundSound,
		AddBackgroundImage,
		EditBackgroundImage,
		DeleteBackgroundImage,
		ClickSoundActivated,
		AddClickSound,
		EditClickSound,
		DeleteClickSound,
	}
	
	
	private JTextField nameField;
	private JTextField movieWidthField;
	private JTextField movieHeightField;
	private JComboBox clickSoundCombo;
	private JSlider clickSoundVolumeSlider;
	private JComboBox backgroundSoundCombo;
	private JSlider backgroundSoundVolumeSlider;
	private JComboBox backgroundImageCombo;
	private JLabel backgroundColorLabel;
	private JButton backgroundColorButton;
	private JButton clearBackgroundColorButton;
	private JComboBox splashScreenCombo;
	private JComboBox maxLinksCombo;
	private JCheckBox randomLinkCheck;
	private JCheckBox keepLinksCheck;
	
	private Color backgroundColorModel = null;
	
	public MovieSettingsPanel()
	{
		createUIComponents();
		layoutUIComponents();
		initListeners();
	}
	protected void createUIComponents()
	{
		IUIFactory uifac = UIFactory.getFactory();
		
		add(uifac.createLabel("projectNameLabel", LanguageBundle.getString("projectsettings.projectname.label")));
		add(nameField = uifac.createTextField("projectNameField"));
		
		add(uifac.createLabel("movieSizeLabel", LanguageBundle.getString("projectsettings.moviesize.label")));
		add(movieWidthField = uifac.createTextField("movieWidthField"));
		add(movieHeightField = uifac.createTextField("movieHeightField"));
		add(uifac.createLabel("movieSizeXLabel", LanguageBundle.getString("projectsettings.moviesizex.label")));
		
		add(uifac.createLabel("clickSoundLabel", LanguageBundle.getString("projectsettings.clicksound.label")));
		add(clickSoundVolumeSlider = uifac.createHorizontalSlider("clickSoundVolumeSlider", 0, 100, 100));
		add(clickSoundCombo = uifac.createComboBox("clickSoundCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));

		add(uifac.createLabel("backgroundSoundLabel", LanguageBundle.getString("projectsettings.backgroundsound.label")));
		add(backgroundSoundVolumeSlider = uifac.createHorizontalSlider("backgroundSoundVolumeSlider", 0, 100, 100));
		add(backgroundSoundCombo = uifac.createComboBox("backgroundSoundCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));
		
		add(uifac.createLabel("backgroundImageLabel", LanguageBundle.getString("projectsettings.backgroundimage.label")));
		add(backgroundImageCombo = uifac.createComboBox("backgroundImageCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));
		add(backgroundColorButton = uifac.createButton("backgroundColorButton", "Color"));
		add(clearBackgroundColorButton = uifac.createButton("clearBackgroundColorButton", UIResourceManager.getIcon(UIResourceManager.ICON_DELETE)));
		add(backgroundColorLabel = uifac.createLabel("backgroundColorLabel", ""));
		backgroundColorLabel.setOpaque(true);
		
		add(uifac.createLabel("splashScreenLabel", LanguageBundle.getString("projectsettings.startscreen.label")));
		add(splashScreenCombo = uifac.createComboBox("splashScreenCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));
		
		add(uifac.createLabel("maxLinksLabel", LanguageBundle.getString("projectsettings.maxlinks.label")));
		add(maxLinksCombo = uifac.createComboBox("maxLinksComboBox", new DefaultComboBoxModel(SnuResourceView.MAXLINKS_CHOICES)));
		maxLinksCombo.setEditable(true);
		
		add(uifac.createLabel("randomLinkLabel", LanguageBundle.getString("projectsettings.randomlink.label")));
		add(randomLinkCheck = uifac.createCheckBox("randomLinkCheck"));
		
		JLabel keepLinksLabel;
		add(keepLinksLabel = uifac.createLabel("keepLinksLabel", LanguageBundle.getString("projectsettings.keeplinks.label")));
		add(keepLinksCheck = uifac.createCheckBox("keepLinksCheck"));
		keepLinksLabel.setToolTipText(LanguageBundle.getString("projectsettings.keeplinks.tooltip"));
		keepLinksCheck.setToolTipText(LanguageBundle.getString("projectsettings.keeplinks.tooltip"));
	}
	protected void layoutUIComponents()
	{
		IUIFactory uifac = UIFactory.getFactory();
		LayoutManager layout = uifac.createLayout("moviesettings");
		setLayout(layout);
	}
	private void initListeners()
	{
		NumberRangeTextFieldConstraint constraint = new NumberRangeTextFieldConstraint(Integer.class, 1, 99999);
		constraint.addAsListenerTo(movieWidthField);
		constraint.addAsListenerTo(movieHeightField);
		
		clearBackgroundColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setBackgroundColor(null);
			}
		});
	}
	public String getNameFieldText()
	{
		return nameField.getText();
	}
	public void setNameFieldText(String name)
	{
		nameField.setText(name);
	}
	public int getMovieWidth()
	{
		return Integer.parseInt(movieWidthField.getText());
	}
	public void setMovieWidth(int width)
	{
		movieWidthField.setText(""+width);
	}
	public int getMovieHeight()
	{
		return Integer.parseInt(movieHeightField.getText());
	}
	public void setMovieHeight(int height)
	{
		movieHeightField.setText(""+height);
	}
	public void setBackgroundSoundChoices(Collection<ISound> sounds)
	{
		backgroundSoundCombo.setModel(new ResourceComboBoxModel(sounds, true));
	}
	public void setBackgroundSound(ISound sound)
	{
		backgroundSoundCombo.setSelectedItem(sound);
	}
	public Long getBackgroundSoundId()
	{
		return getBackgroundSound()!=null?getBackgroundSound().getId():null;
	}
	private ISound getBackgroundSound()
	{
		return (ISound)backgroundSoundCombo.getSelectedItem();
	}
	public float getBackgroundSoundVolume()
	{
		return backgroundSoundVolumeSlider.getValue()/100.0f;
	}
	public void setBackgroundSoundVolume(float volume)
	{
		backgroundSoundVolumeSlider.setValue((int)(100*volume));
	}
	public void setBackgroundImageChoices(Collection<IImage> images)
	{
		backgroundImageCombo.setModel(new ResourceComboBoxModel(images, true));
	}
	public void setBackgroundImage(IImage image)
	{
		backgroundImageCombo.setSelectedItem(image);
	}
	public void addBackgroundColorActionListener(ActionListener listener)
	{
		backgroundColorButton.addActionListener(listener);
	}
	public Color getBackgroundColor()
	{
		return backgroundColorModel;
	}
	public void setBackgroundColor(Color color)
	{
		backgroundColorModel = color;
		backgroundColorLabel.setBackground(color);
	}
	public Long getBackgroundImageId()
	{
		return getBackgroundImage()!=null?getBackgroundImage().getId():null;
	}
	private IImage getBackgroundImage()
	{
		return (IImage)backgroundImageCombo.getSelectedItem();
	}
	public void setClickSoundChoices(Collection<ISound> sounds)
	{
		clickSoundCombo.setModel(new ResourceComboBoxModel(sounds, true));
	}
	public Long getClickSoundId()
	{
		return getClickSound()!=null?getClickSound().getId():null;
	}
	public void setClickSound(ISound sound)
	{
		clickSoundCombo.setSelectedItem(sound);
	}
	private ISound getClickSound()
	{
		return (ISound)clickSoundCombo.getSelectedItem();
	}
	public float getClickSoundVolume()
	{
		return clickSoundVolumeSlider.getValue()/100.0f;
	}
	public void setClickSoundVolume(float volume)
	{
		clickSoundVolumeSlider.setValue((int)(100*volume));
	}
	public Long getSplashScreenMediaId()
	{
		return getSplashScreenMedia()!=null?getSplashScreenMedia().getId():null;
	}
	private IMedia getSplashScreenMedia()
	{
		return (IMedia)splashScreenCombo.getSelectedItem();
	}
	public void setSplashScreenMedia(Object media)
	{
		splashScreenCombo.setSelectedItem(media);
	}
	public void setSplashScreenMediaChoices(Collection<? extends IMedia> media)
	{
		splashScreenCombo.setModel(new ResourceComboBoxModel(media, true));
	}
	public void setRandomLinkMode(boolean randomLinkMode)
	{
		randomLinkCheck.setSelected(randomLinkMode);
	}
	public boolean getRandomLinkMode()
	{
		return randomLinkCheck.isSelected();
	}
	public void setKeepLinksOnEmptySearch(boolean keepLinks)
	{
		keepLinksCheck.setSelected(keepLinks);
	}
	public boolean getKeepLinksOnEmptySearch()
	{
		return keepLinksCheck.isSelected();
	}
	public Long getMaxLinks()
	{
		return SnuResourceView.getComboLong(maxLinksCombo, SnuResourceView.DEFAULT_MAXLINKS);
	}
	public void setMaxLinks(Long maxLinks)
	{
		maxLinksCombo.setSelectedItem(maxLinks!=null?(""+maxLinks):""+SnuResourceView.INFINITE);
	}
	public void dispose()
	{
	}
}
