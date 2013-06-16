package org.korsakow.ide.ui.interfacebuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.TokenizerTextArea;
import org.korsakow.ide.ui.components.cell.ResourceDOComboBoxRenderer;
import org.korsakow.ide.ui.components.model.ResourceComboBoxModel;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.ui.interfacebuilder.panel.ArrangePanel;
import org.korsakow.ide.ui.interfacebuilder.panel.GridInfoPanel;
import org.korsakow.ide.ui.interfacebuilder.panel.InfoPanel;
import org.korsakow.ide.ui.interfacebuilder.panel.WidgetsPanel;
import org.korsakow.ide.ui.resources.ResourceView;
import org.korsakow.ide.util.UIHelper;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;


public class InterfaceBuilderMainPanel extends ResourceView
{
	private JPanel topPanel;
	private JPanel toolsPanel;
	private JPanel settingsPanel;
	
	private WidgetCanvas canvas;
	private JScrollPane canvasScroll;
	
	private WidgetsPanel widgetsPanel;
	private JButton deleteButton;
	private JComboBox clickSoundCombo;
	private JSlider clickSoundVolumeSlider;
	private JComboBox backgroundImageCombo;
	private JLabel backgroundColorLabel;
	private JButton backgroundColorButton;
	private JButton clearBackgroundColorButton;
	private JToggleButton snapToGridButton;
	private InfoPanel infoPanel;
	private ArrangePanel arrangePanel;
	private GridInfoPanel gridInfoPanel;
	private KCollapsiblePane propertiesCollapse;
	
	private Color backgroundColorModel = null;
	
	private JCheckBox showBackgroundCheck;
	
//	private ISound clickSound;
	
	private IUIFactory uifac;
	
	public InterfaceBuilderMainPanel()
	{
	}
	@Override
	protected void initUI()
	{
//		super.initUI();
		
		uifac = UIFactory.getFactory();
		
		setLayout(new BorderLayout());
		// hack until ui is consistent accross the whole app
		setBackground(UIManager.getColor("window2"));
		setOpaque(true);
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				if (getTopLevelAncestor() != null)
					((JFrame)getTopLevelAncestor()).getContentPane().setBackground(getBackground());
			}
		});
		// /hack
		
		createTopPanel();
		add(topPanel, BorderLayout.NORTH);
		
		createMainCanvas();
		add(mainPanel, BorderLayout.CENTER);
		
		tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.EAST);
		
		createToolsTab();
		tabbedPane.addTab(LanguageBundle.getString("interfacebuilder.tab.tools.label"), toolsPanel);
		
		settingsPanel = createSettingsPanel();
		tabbedPane.addTab(LanguageBundle.getString("interfacebuilder.tab.settings.label"), settingsPanel);
		
		toolsPanel.add(Box.createVerticalGlue());
		toolsPanel.setMaximumSize(new Dimension(250, Short.MAX_VALUE));
		toolsPanel.setPreferredSize(new Dimension(250, 100));
		
		settingsPanel.add(Box.createVerticalGlue());
		settingsPanel.setMaximumSize(new Dimension(200, Short.MAX_VALUE));
		settingsPanel.setPreferredSize(new Dimension(200, 100));
	}
	
	protected void createTopPanel()
	{
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		statusArea = uifac.createTextArea("statusArea");
		statusArea.setEditable(false);
		statusArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		statusArea.setBackground(Color.white);
		statusArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		statusArea.setVisible(false);
		topPanel.add(statusArea);
	}
	
	protected void createMainCanvas()
	{
		mainPanel = new JPanel(new BorderLayout());
		
		canvas = new WidgetCanvas();
		canvasScroll = new JScrollPane(canvas) {
			@Override
			public Dimension getPreferredSize() {
				// somehow avoids a scrollbar appearing when not necessary on initial show
				Dimension d = super.getPreferredSize();
				d.width += canvas.getModel().getGridWidth();
				d.height += canvas.getModel().getGridHeight();
				return d;
			}
		};
		mainPanel.add(canvasScroll, BorderLayout.CENTER);
	}
	
	protected void createToolsTab()
	{
		toolsPanel = new JPanel();
		toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.Y_AXIS));
		
		toolsPanel.add(Box.createVerticalStrut(25));
		
		infoPanel = new InfoPanel();
		infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 25));
		KCollapsiblePane infoCollapse = new KCollapsiblePane(LanguageBundle.getString("interfacebuilder.infopanel.label"), infoPanel);
		infoCollapse.setAutoSetMaxSize(true);
		toolsPanel.add(infoCollapse);
		
		arrangePanel = new ArrangePanel();
		arrangePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		KCollapsiblePane arrangeCollapse = new KCollapsiblePane(LanguageBundle.getString("interfacebuilder.arrangepanel.label"), arrangePanel);
		arrangeCollapse.setAutoSetMaxSize(true);
		toolsPanel.add(arrangeCollapse);
		
		propertiesCollapse = new KCollapsiblePane(LanguageBundle.getString("interfacebuilder.propertiespanel.label"), new JPanel());
		toolsPanel.add(propertiesCollapse);
		
		JPanel widgetsHolderPane = new JPanel(new BorderLayout());
		widgetsHolderPane.add(new JLabel("Drag to stage", JLabel.CENTER), BorderLayout.NORTH);
		widgetsPanel = new WidgetsPanel();
		widgetsHolderPane.add(widgetsPanel, BorderLayout.CENTER);
		KCollapsiblePane widgetsCollapse = new KCollapsiblePane(LanguageBundle.getString("interfacebuilder.widgetspanel.label"), widgetsHolderPane);
		widgetsCollapse.setAutoSetMaxSize(true);
		toolsPanel.add(widgetsCollapse);
		
		toolsPanel.add(Box.createVerticalGlue());

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		deleteButton = uifac.createButton("deleteButton", UIResourceManager.getIcon(UIResourceManager.ICON_DELETE));	
		buttonPanel.add(deleteButton, BorderLayout.EAST);
		
		//Snap to grid toggle Button
		snapToGridButton = uifac.createToggleButton("snapToGridButton","Snap to Grid", UIResourceManager.getIcon(UIResourceManager.ICON_DELETE), true);
		buttonPanel.add(snapToGridButton, BorderLayout.EAST);
		toolsPanel.add(buttonPanel);
	}
	
	
	@Override
	protected void initListeners()
	{
		super.initListeners();
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.getModel().removeWidgets(canvas.getSelectionModel().getSelectedWidgets());
			}
		});
		
		snapToGridButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				canvas.getModel().toggleSnapToGrid();
			}
		});
	}
	public WidgetCanvas getCanvas()
	{
		return canvas;
	}
	public JScrollPane getCanvasScroll()
	{
		return canvasScroll;
	}
	public InfoPanel getInfoPanel()
	{
		return infoPanel;
	}
	public ArrangePanel getArrangePanel()
	{
		return arrangePanel;
	}
	public void setPropertiesEditor(JComponent editor)
	{
		propertiesCollapse.setContent(editor);
	}
	public GridInfoPanel getGridInfoPanel()
	{
		return gridInfoPanel;
	}
	public void setClickSoundChoices(Collection<ISound> sounds)
	{
		clickSoundCombo.setModel(new ResourceComboBoxModel(sounds, true));
	}
	public void setClickSound(ISound sound)
	{
		clickSoundCombo.setSelectedItem(sound);
	}
	public Long getClickSoundId()
	{
		return getClickSound()!=null?getClickSound().getId():null;
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
	
	public void setShowBackground(boolean show)
	{
		showBackgroundCheck.setSelected(show);
	}
	public boolean getShowBackground()
	{
		return showBackgroundCheck.isSelected();
	}
	public void addShowBackgroundListener(ActionListener listener)
	{
		showBackgroundCheck.addActionListener(listener);
	}
	
	@Override
	public void dispose()
	{
//		if (clickSound != null) {
//			clickSoundPanel.setResource(null);
//		}
	}
	
	protected JPanel createSettingsPanel()
	{
		JPanel settingsPanel = new JPanel();
		
		settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
		settingsPanel.add(Box.createVerticalStrut(25));
		
		JPanel interfPanel = UIHelper.createVerticalBoxLayoutPanel(
			UIHelper.createBorderLayoutLabelPanel(LanguageBundle.getString("interfacebuilder.name.label"), nameField = new JTextField())
		);
		interfPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		KCollapsiblePane interfCollapse = new KCollapsiblePane("Interface", interfPanel);
		settingsPanel.add(interfCollapse);
		
		uifac.createLabel("keywordLabel", LanguageBundle.getString("interfacebuilder.keywords.label"));
		inKeywordBox = new TokenizerTextArea();
		inKeywordBox.setName("inKeywordBox");
		
		KCollapsiblePane gridCollapse = new KCollapsiblePane(LanguageBundle.getString("interfacebuilder.gridinfo.label"), gridInfoPanel = new GridInfoPanel());
		settingsPanel.add(gridCollapse);
		gridCollapse.setName("gridInfoPanel");
		
		KCollapsiblePane mediaCollapse = new KCollapsiblePane("Media", new JPanel());
		settingsPanel.add(mediaCollapse);
		mediaCollapse.getContent().setLayout(uifac.createLayout("interface_media"));
		mediaCollapse.getContent().setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		mediaCollapse.getContent().add(uifac.createLabel("clickSoundLabel", LanguageBundle.getString("interfacebuilder.clicksound.label")));
		mediaCollapse.getContent().add(clickSoundVolumeSlider = uifac.createHorizontalSlider("clickSoundVolumeSlider", 0, 100, 100));
		mediaCollapse.getContent().add(clickSoundCombo = uifac.createComboBox("clickSoundCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));
		clickSoundCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setClickSound((ISound)clickSoundCombo.getSelectedItem());
			}
		});
		mediaCollapse.getContent().add(uifac.createLabel("backgroundImageLabel", LanguageBundle.getString("projectsettings.backgroundimage.label")));
		mediaCollapse.getContent().add(backgroundImageCombo = uifac.createComboBox("backgroundImageCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));
		mediaCollapse.getContent().add(backgroundColorButton = uifac.createButton("backgroundColorButton", "Color"));
		mediaCollapse.getContent().add(clearBackgroundColorButton = uifac.createButton("clearBackgroundColorButton", UIResourceManager.getIcon(UIResourceManager.ICON_DELETE)));
		mediaCollapse.getContent().add(backgroundColorLabel = uifac.createLabel("backgroundColorLabel", ""));
		backgroundColorLabel.setOpaque(true);
		
		KCollapsiblePane previewCollapse = new KCollapsiblePane("Preview", UIHelper.createVerticalBoxLayoutPanel(
			UIHelper.createLabelPanel("Show Background", showBackgroundCheck = new JCheckBox())
		));
		previewCollapse.getContent().setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		settingsPanel.add(previewCollapse);
		
		return settingsPanel;
	}
	public void setBackgroundImageChoices(Collection<IImage> images)
	{
		backgroundImageCombo.setModel(new ResourceComboBoxModel(images, true));
	}
	public void setBackgroundImage(IImage image)
	{
		backgroundImageCombo.setSelectedItem(image);
	}
	public void addBackgroundImageActionListener(ActionListener listener)
	{
		backgroundImageCombo.addActionListener(listener);
	}
	public void addBackgroundColorActionListener(ActionListener listener)
	{
		backgroundColorButton.addActionListener(listener);
	}
	public void addClearBackgroundColorActionListener(ActionListener listener)
	{
		clearBackgroundColorButton.addActionListener(listener);
	}
	public Color getBackgroundColor()
	{
		return backgroundColorModel;
	}
	public void setBackgroundColorModel(Color color)
	{
		backgroundColorModel = color;
		backgroundColorLabel.setBackground(color);
	}
	public Long getBackgroundImageId()
	{
		return getBackgroundImage()!=null?getBackgroundImage().getId():null;
	}
	public IImage getBackgroundImage()
	{
		return (IImage)backgroundImageCombo.getSelectedItem();
	}
}
