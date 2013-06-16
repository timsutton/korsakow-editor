package org.korsakow.ide.ui.resources;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.DelayedMediaPanelLoader;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.KLayoutPanel;
import org.korsakow.ide.ui.components.NewMediaPanel;
import org.korsakow.ide.ui.components.TokenizerTextArea;
import org.korsakow.ide.ui.components.cell.ResourceDOComboBoxRenderer;
import org.korsakow.ide.ui.components.code.CodeTable;
import org.korsakow.ide.ui.components.layout.HorizontalFlowLayout;
import org.korsakow.ide.ui.components.layout.VerticalFlowLayout;
import org.korsakow.ide.ui.components.model.ResourceComboBoxModel;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.util.PreferencesManager;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

public class SnuResourceView extends ResourceView
{
	/**
	 * TODO: refactor
	 */
	public static final String INFINITE = "\u221E";
	private static final String[] LIVES_CHOICES = {"1", "2", "3", "4", "5", INFINITE};
	private static final Long DEFAULT_LIVES = 1L;

	/**
	 * TODO: refactor
	 */
	public static final String[] MAXLINKS_CHOICES = {"1", "2", "3", "4", "5", INFINITE};
	/**
	 * TODO: refactor
	 */
	public static final Long DEFAULT_MAXLINKS = null;
	
	
	private Long mainMediaCustomDuration;
	private NewMediaPanel mainMediaPanel;
	private Long mainMediaId;
	private JCheckBox videoLoopCheck;
	private JComboBox backgroundSoundCombo;
	private JCheckBox backgroundSoundLoopCheck;
	private JComboBox interfaceCombo;
	
	JComboBox livesCombo;
	DefaultComboBoxModel livesComboModel;
	private JComboBox maxLinksCombo;
	private DefaultComboBoxModel maxLinksComboModel;
	
	private JCheckBox startingSnuCheck;
	private JCheckBox endingSnuCheck;
	
	private JComboBox previewMediaCombo;
	private NewMediaPanel previewMediaPanel;
	private JTextField previewTextField;
	
	private JTextField insertTextField;
	
	private CodeTable codeTable;
	
	private JSplitPane mainSplitPane;
	
	private KCollapsiblePane snuCollapse;
	private KCollapsiblePane keywordCollapse;
	private KCollapsiblePane previewCollapse;
	
	private JSlider ratingSlider;
	private JLabel ratingField;
	
	private Object cachedRules;
	
	public SnuResourceView()
	{
	}

	@Override
	protected void createUIComponents()
	{
		// hack until ui is consistent accross the whole app
		setBackground(UIManager.getColor("window2"));
		setOpaque(true);
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				if (getTopLevelAncestor() != null) // timing issues...
					((JFrame)getTopLevelAncestor()).getContentPane().setBackground(getBackground());
			}
		});
		// /hack
		super.createUIComponents();
		IUIFactory uifac = UIFactory.getFactory();
//		setLayout(null);
		removeAll();
		JPanel dummyPanel = new KLayoutPanel();
		mainPanel = new KLayoutPanel();
		add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		VerticalFlowLayout vlayout = new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0);
		vlayout.setMaximizeOtherDimension(true);
		mainPanel.setLayout(vlayout);

		
		JPanel snuPanel = new KLayoutPanel();
		JPanel keywordPanel = new KLayoutPanel();
		JPanel previewPanel = new KLayoutPanel();
		
		dummyPanel.add(maxLinksCombo = uifac.createComboBox("maxLinksComboBox", maxLinksComboModel = new DefaultComboBoxModel(MAXLINKS_CHOICES)));
		
		JPanel snuPanelSizer = uifac.customComponent("snuSettingPanelSizer", new JPanel());
		//This causes the snuPanel to stretch nicely. I make it invisible not to interfere with the real widgets.
		//This presumes that the size of an invisible panel is considered when sizing snuPanel.
		snuPanelSizer.setVisible(false);
		snuPanel.add(snuPanelSizer);
		
		snuPanel.add(uifac.createLabel("backgroundSoundLabel", LanguageBundle.getString("snuresourceview.backgroundsound.label")));
		snuPanel.add(backgroundSoundCombo = uifac.createComboBox("backgroundSoundPanel", new ResourceComboBoxModel(false), new ResourceDOComboBoxRenderer()));
		backgroundSoundCombo.setEditable(false);
//		snuPanel.add(uifac.createLabel("backgroundSoundLoopLabel", LanguageBundle.getString("snuresourceview.mainmediapanel.backgroundsoundloop.label")));
		snuPanel.add(backgroundSoundLoopCheck = uifac.createCheckBox("backgroundSoundLoopCheck", LanguageBundle.getString("snuresourceview.backgroundsound.loop.label")));
		backgroundSoundLoopCheck.setHorizontalTextPosition(JCheckBox.LEFT);
		snuPanel.add(uifac.createLabel("livesLabel", LanguageBundle.getString("snuresourceview.lives.label")));
		snuPanel.add(livesCombo = uifac.createComboBox("livesComboBox", livesComboModel = new DefaultComboBoxModel(LIVES_CHOICES)));
		livesCombo.setEditable(true);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBounds(10, 95, 500, 23);
		snuPanel.add(bottomPanel);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
//		bottomPanel.setLayout(new HorizontalFlowLayout(HorizontalFlowLayout.LEFT));
		bottomPanel.add(uifac.createLabel("ratingLabel", LanguageBundle.getString("snuresourceview.rating.label")));
		bottomPanel.add(Box.createHorizontalStrut(5));
		ratingSlider = uifac.createSlider("ratingSlider", new DefaultBoundedRangeModel(0, 0, 0, 100));
		ratingSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				ratingField.setText(String.format("%2.2f", (RatingCalculator.calculate(ratingSlider.getValue()/100.0f))));
			}
		});
		ratingField = uifac.createLabel("ratingField");
		ratingField.setHorizontalAlignment(JLabel.CENTER);
		ratingField.setBorder(BorderFactory.createLineBorder(Color.gray));
		JPanel ratingBorder = new JPanel(new HorizontalFlowLayout(HorizontalFlowLayout.LEFT, 0, 0));
		ratingBorder.setLayout(new BoxLayout(ratingBorder, BoxLayout.X_AXIS));
		ratingBorder.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.gray),
				BorderFactory.createEmptyBorder(3, 5, 3, 0)
		));
		bottomPanel.add(ratingBorder);
		ratingBorder.add(ratingSlider);
		ratingBorder.add(ratingField);
		bottomPanel.add(Box.createHorizontalStrut(100));
		bottomPanel.add(Box.createHorizontalGlue());
//		bottomPanel.add(ratingBorder = uifac.customComponent("ratingBorder", new JPanel()));
//		ratingBorder.setBorder(BorderFactory.createLineBorder(Color.gray));
		ratingSlider.setValue(10);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		snuPanel.add(topPanel);
		topPanel.setBounds(10, 14, 500, 23);
		
		topPanel.add(uifac.createLabel("startingSnuLabel", LanguageBundle.getString("snuresourceview.startingsnu.label")));
		topPanel.add(Box.createHorizontalStrut(5));
		topPanel.add(startingSnuCheck = uifac.createCheckBox("startingSnuCheckBox"));
		
		topPanel.add(Box.createHorizontalStrut(20));
		
		topPanel.add(uifac.createLabel("endingSnuLabel", LanguageBundle.getString("snuresourceview.endingsnu.label")));
		topPanel.add(Box.createHorizontalStrut(5));
		topPanel.add(endingSnuCheck = new JCheckBox());
		
		topPanel.add(Box.createHorizontalStrut(20));
		
		topPanel.add(new JLabel(LanguageBundle.getString("snuresourceview.mainmediapanel.loop.label")));
		topPanel.add(Box.createHorizontalStrut(5));
		topPanel.add(videoLoopCheck = new JCheckBox());
		
		topPanel.add(Box.createHorizontalGlue());
		
		snuPanel.add(uifac.createLabel("interfaceLabel", LanguageBundle.getString("snuresourceview.interface.label")));
		snuPanel.add(interfaceCombo = uifac.createComboBox("interfacePanel", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));

		snuPanel.add(uifac.createLabel("insertTextLabel", LanguageBundle.getString("snuresourceview.inserttext.label")));
		snuPanel.add(insertTextField = uifac.createTextField("insertTextField"));
		
		JPanel keywordPanelSizer = uifac.customComponent("keywordPanelSizer", new JPanel());
		keywordPanelSizer.setVisible(false);
		keywordPanel.add(keywordPanelSizer);
		
		codeTable = new CodeTable();
		JScrollPane codeScroll = uifac.customComponent("k3Rules", new JScrollPane(codeTable));
		codeScroll.setPreferredSize(new Dimension(600, 200));
		// setting the border results in the LaF's default border, not no border
		codeScroll.setBorder(BorderFactory.createEmptyBorder());
		// stop cell editing when clicking outside the table bounds
		codeScroll.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent event) {
				if (codeTable.getCellEditor() != null)
					codeTable.getCellEditor().stopCellEditing();
			}
		});
		
		keywordPanel.add(uifac.createLabel("k3RulesLabel", LanguageBundle.getString("snuresourceview.outkeywords.label"), UIResourceManager.getIcon(UIResourceManager.ICON_SNU_OUT)));
		keywordPanel.add(codeScroll);
		keywordPanel.add(inKeywordLabel);
		keywordPanel.add(inKeywordBox);
		inKeywordBox.setFocusBackgroundColor(new Color(0xee1b23));
		inKeywordBox.setFocusForegroundColor(Color.white);
		inKeywordBox.setBorder(new JTextField().getBorder());
		
		JPanel previewPanelSizer = uifac.customComponent("previewPanelSizer", new JPanel());
		previewPanelSizer.setVisible(false);
		previewPanel.add(previewPanelSizer);
		
		previewPanel.add(uifac.createLabel("previewMediaLabel", LanguageBundle.getString("snuresourceview.previewmedia.label")));
		previewPanel.add(previewMediaCombo = uifac.createComboBox("previewMediaCombo", new ResourceComboBoxModel(true), new ResourceDOComboBoxRenderer()));
		previewMediaCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setPreviewMedia(getPreviewMedia());
			}
		});
		previewPanel.add(previewMediaPanel = uifac.customComponent("previewMediaPanel", new NewMediaPanel()));
		previewPanel.add(uifac.createLabel("previewTextLabel", LanguageBundle.getString("snuresourceview.previewtext.label")));
		previewPanel.add(previewTextField = uifac.createTextField("previewTextField"));
		
		mainPanel.add(nameField);
		mainPanel.add(mainMediaPanel = uifac.customComponent("mainMedia", new NewMediaPanel()));
//		mainPanel.add(mainSplitPane = uifac.createSplitPane("mainSplitPane"));
		mainPanel.add(keywordCollapse = uifac.customComponent("keywordPanel", new KCollapsiblePane(LanguageBundle.getString("resourceview.tab.keywords.label"), keywordPanel)));
		mainPanel.add(previewCollapse = uifac.customComponent("previewPanel", new KCollapsiblePane(LanguageBundle.getString("snuresourceview.tab.preview.label"), previewPanel)));
		mainPanel.add(snuCollapse = uifac.customComponent("snuPanel", new KCollapsiblePane(LanguageBundle.getString("snuresourceview.snusettings.label"), snuPanel)));
		UIUtil.runUITaskLater(new Runnable() { // this is run later because we set the windows' size based on its panel's preferred size and we want the full size
			public void run() {
				snuCollapse.setExpanded(PreferencesManager.getPreferences(SnuResourceView.class).getBoolean("snuCollapse", true));
				keywordCollapse.setExpanded(PreferencesManager.getPreferences(SnuResourceView.class).getBoolean("keywordCollapse", true));
				previewCollapse.setExpanded(PreferencesManager.getPreferences(SnuResourceView.class).getBoolean("previewCollapse", true));
			}
		});
		
	}
	@Override
	protected void layoutUIComponents()
	{
		super.layoutUIComponents();
		IUIFactory uifac = UIFactory.getFactory();
		LayoutManager layout = uifac.createLayout("snuresourceview");
		snuCollapse.getContent().setLayout(layout);
		keywordCollapse.getContent().setLayout(layout);
		previewCollapse.getContent().setLayout(layout);
		Dimension mainMediaSize = new Dimension(481, 270);
		mainMediaPanel.setPreferredSize(mainMediaSize);
		mainMediaPanel.setMaximumSize(mainMediaSize);
		mainMediaPanel.setMinimumSize(mainMediaSize);
	}
	
	@Override
	protected void initUI()
	{
		super.initUI2();
	}

	@Override
	protected void initListeners()
	{
		super.initListeners();
		
		livesCombo.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				onLivesFocusLost();
			}
		});
		maxLinksCombo.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				onMaxLinksFocusLost();
			}
		});
		
	}
	public TokenizerTextArea getKeywordArea()
	{
		return inKeywordBox;
	}
	public CodeTable getCodeTable()
	{
		return codeTable;
	}
	public NewMediaPanel getMainMediaPanel()
	{
		return mainMediaPanel;
	}
	private void onLivesFocusLost()
	{
		Object value = livesCombo.getSelectedItem();
		if (value == null)
			livesCombo.setSelectedItem(INFINITE);
		else {
			try {
				livesCombo.setSelectedItem(Long.parseLong(value.toString()));
			} catch (NumberFormatException e) {
				livesCombo.setSelectedItem(INFINITE);
			}
		}
	}
	private void onMaxLinksFocusLost()
	{
		Object value = maxLinksCombo.getSelectedItem();
		if (value == null)
			maxLinksCombo.setSelectedItem(INFINITE);
		else {
			try {
				maxLinksCombo.setSelectedItem(Long.parseLong(value.toString()));
			} catch (NumberFormatException e) {
				maxLinksCombo.setSelectedItem(INFINITE);
			}
		}
	}

	public void setMainMedia(final IMedia media)
	{
		mainMediaId = media.getId();
		DelayedMediaPanelLoader.load(this, mainMediaPanel, media);
	}
	public Long getMainMediaId()
	{
		return mainMediaId;
	}
	public void setInterfaceChoices(Collection<IInterface> choices)
	{
		interfaceCombo.setModel(new ResourceComboBoxModel(choices, false));
	}
	public void setInterface(IInterface interf)
	{
		interfaceCombo.setSelectedItem(interf);
		repaint();
	}
	private IInterface getInterface()
	{
		return (IInterface)interfaceCombo.getSelectedItem();
	}
	public Long getInterfaceId()
	{
		return getInterface()!=null?getInterface().getId():null;
	}
	public void setBackgroundSoundChoices(Collection<ISound> choices)
	{
		List<Object> c = new ArrayList<Object>();
		c.add(BackgroundSoundMode.KEEP);
		c.add(BackgroundSoundMode.CLEAR);
		c.addAll(choices);
		backgroundSoundCombo.setModel(new ResourceComboBoxModel(c));
	}
	public void setBackgroundSound(Object sound)
	{
		backgroundSoundCombo.setSelectedItem(sound);
	}
	public BackgroundSoundMode getBackgroundSoundMode()
	{
		if ( backgroundSoundCombo.getSelectedItem() instanceof IResource &&
			ResourceType.SOUND.isInstance( (IResource)backgroundSoundCombo.getSelectedItem() ))
			return BackgroundSoundMode.SET;
		else
			return (BackgroundSoundMode)backgroundSoundCombo.getSelectedItem();
	}
	/**
	 * Valid to call only when BackgroundSoundMode == SET
	 */
	public Long getBackgroundSoundId()
	{
		return ((ISound)backgroundSoundCombo.getSelectedItem()).getId();
	}
	public void addBackgroundSoundChangeListener( ActionListener listener ) {
		backgroundSoundCombo.addActionListener( listener );
	}
	public boolean getBackgroundSoundLooping()
	{
		return backgroundSoundLoopCheck.isSelected();
	}
	public void setBackgroundSoundLoop(boolean loop)
	{
		backgroundSoundLoopCheck.setSelected(loop);
	}
	public void setPreviewMediaChoices(Collection<IMedia> choices)
	{
		previewMediaCombo.setModel(new ResourceComboBoxModel(choices, true));
	}
	public void setPreviewMedia(final IMedia media)
	{
		previewMediaCombo.setSelectedItem(media);
		DelayedMediaPanelLoader.load(this, previewMediaPanel, media);
	}
	private IMedia getPreviewMedia()
	{
		return (IMedia)previewMediaCombo.getSelectedItem();
	}
	public Long getPreviewMediaId()
	{
		return getPreviewMedia()!=null?getPreviewMedia().getId():null;
	}
	public String getPreviewText()
	{
		return previewTextField.getText();
	}
	public void setPreviewText(String text)
	{
		previewTextField.setText(text);
	}
	public String getInsertText()
	{
		return insertTextField.getText();
	}
	public void setInsertText(String text)
	{
		insertTextField.setText(text);
	}
	public Long getLives()
	{
		return getComboLong(livesCombo, DEFAULT_LIVES);
	}
	public void setLives(Long lives)
	{
		livesCombo.setSelectedItem(lives!=null?(""+lives):""+INFINITE);
	}
	public Long getMaxLinks()
	{
		return getComboLong(maxLinksCombo, DEFAULT_MAXLINKS);
	}
	public void setMaxLinks(Long maxLinks)
	{
		maxLinksCombo.setSelectedItem(maxLinks!=null?(""+maxLinks):""+INFINITE);
	}
	public void setRating(float rating)
	{
		ratingSlider.setValue((int)(RatingCalculator.inverse(rating)*100));
	}
	public float getRating()
	{
		return RatingCalculator.calculate(ratingSlider.getValue()/100.0f);
	}
	public void setLooping(boolean loop)
	{
		videoLoopCheck.setSelected(loop);
	}
	public boolean getLooping()
	{
		return videoLoopCheck.isSelected();
	}
	public JCheckBox getStarerCheck()
	{
		return startingSnuCheck;
	}
	public boolean getStarter()
	{
		return startingSnuCheck.isSelected();
	}
	public void setStarter(boolean starter)
	{
		startingSnuCheck.setSelected(starter);
	}
	public JCheckBox getEnderCheck()
	{
		return endingSnuCheck;
	}
	public boolean getEnder()
	{
		return endingSnuCheck.isSelected();
	}
	public void setEnder(boolean ender)
	{
		endingSnuCheck.setSelected(ender);
	}
	
	public void setCachedRules(Object cachedRules)
	{
		this.cachedRules = cachedRules;
	}
	public Object getCachedRules()
	{
		return cachedRules;
	}
	
	public void setMainMediaCustomDuration( long duration ) {
		mainMediaPanel.setDuration( duration );
		mainMediaCustomDuration = duration;
	}
	public Long getMainMediaCustomDuration() {
		return mainMediaCustomDuration;
	}
	
	/**
	 * TODO: refactor me
	 * @param combo
	 * @param defaultValue
	 * @return
	 */
	public static Long getComboLong(JComboBox combo, Long defaultValue)
	{
		Object value = combo.getSelectedItem();
		if (value == null || value.equals(INFINITE))
			return null;
		else {
			try {
				return Long.parseLong(value.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
	}
	@Override
	public void dispose()
	{
		super.dispose();
		PreferencesManager.getPreferences(SnuResourceView.class).putBoolean("snuCollapse", snuCollapse.isExpanded());
		PreferencesManager.getPreferences(SnuResourceView.class).putBoolean("keywordCollapse", keywordCollapse.isExpanded());
		PreferencesManager.getPreferences(SnuResourceView.class).putBoolean("previewCollapse", previewCollapse.isExpanded());
//		if (mainMedia != null) {
			mainMediaPanel.dispose();
//		}
		previewMediaPanel.dispose();
	}
}
