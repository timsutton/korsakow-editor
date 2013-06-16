package org.korsakow.ide.ui.resources;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Collection;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.KLayoutPanel;
import org.korsakow.ide.ui.components.TokenizerTextArea;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

public abstract class ResourceView extends KLayoutPanel
{
	protected Long resourceId;
	protected JTextArea statusArea;
	protected JLabel nameLabel;
	protected JTextField nameField;
	protected JPanel mainPanel;

	protected JLabel inKeywordLabel;
	protected TokenizerTextArea inKeywordBox;
	protected JTabbedPane tabbedPane;
	
	public ResourceView()
	{
		initUI();
		initListeners();
	}
	protected void createUIComponents()
	{
		IUIFactory uifac = UIFactory.getFactory();
		//add(statusArea = uifac.createTextArea("statusArea"));
		statusArea = uifac.createTextArea("statusArea");
		add(nameLabel = uifac.createLabel("nameLabel"));
		add(nameField = uifac.createTextField("nameField"));
		add(tabbedPane = uifac.createTabbedPane("tabbedPane"));
		add(inKeywordLabel = uifac.createLabel("inKeywordsLabel", LanguageBundle.getString("resourceview.inkeywords.label"), UIResourceManager.getIcon(UIResourceManager.ICON_SNU_IN)));
		add(uifac.customComponent("inKeywords", (inKeywordBox = new TokenizerTextArea())));
	}
	protected void layoutUIComponents()
	{
		IUIFactory uifac = UIFactory.getFactory();
	}
	protected void initUI2()
	{
		createUIComponents();
		layoutUIComponents();
	}
	protected void initUI()
	{
		setLayout(new BorderLayout());
		JPanel topPanel = new KLayoutPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		add(topPanel);

		//add(mainPanel);
//		Box box;
		statusArea = new JTextArea();
		statusArea.setEditable(false);
		statusArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		topPanel.add(statusArea);
		statusArea.setBackground(Color.white);
		statusArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
		statusArea.setVisible(false);
		
		JPanel subPanel;
		Box subBox;
		JPanel namePanel = new KLayoutPanel(new BorderLayout());
		topPanel.add(namePanel);
//		namePanel.add(nameLabel = new JLabel("Name"), BorderLayout.WEST);
		namePanel.add(nameField = new JTextField());
		namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height)); // why is this necesasry?
//		nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getMinimumSize().height)); // why is this necesasry?
		
		tabbedPane = new JTabbedPane();
		mainPanel = new KLayoutPanel();
		mainPanel.setLayout(new BorderLayout());
		tabbedPane.addTab(LanguageBundle.getString("resourceview.tab.resource.label"), mainPanel);
		topPanel.add(tabbedPane);
		inKeywordBox = new TokenizerTextArea();
//		mainPanel.add(keywordBox = new TokenizerTextArea());
//		tabbedPane.addTab(LanguageBundle.getString("resourceview.tab.keywords.label"), keywordBox = new TokenizerTextArea());
	}
	public void setStatusText(String text)
	{
		statusArea.setText(text);
		statusArea.setVisible(text.length()>0);
	}
	public boolean isViewValid()
	{
		return statusArea.getText()==null || statusArea.getText().length() == 0;
	}
	protected void initListeners()
	{
	}
	public void setResourceId(Long id)
	{
		resourceId = id;
	}
	public Long getResourceId()
	{
		return resourceId;
	}
	public String getNameFieldText()
	{
		return nameField.getText();
	}
	public void setNameFieldText(String text)
	{
		nameField.setText(text);
	}
	public Collection<IKeyword> getKeywords()
	{
		Collection<IKeyword> keywords = new TreeSet<IKeyword>();
		Collection<String> tokens = inKeywordBox.getTokens();
		for (String token : tokens)
			keywords.add(KeywordFactory.createNew(token));
		return keywords;
	}
	public void setKeywords(Collection<IKeyword> keywords)
	{
		Collection<String> tokens = new TreeSet<String>();
		for (IKeyword keyword : keywords)
			tokens.add(keyword.getValue());
		setKeywordTokens(tokens);
	}
	public void setKeywordTokens(Collection<String> keywords)
	{
		inKeywordBox.setTokens(keywords);
	}
	public Collection<String> getKeywordTokens(Collection<String> tokens)
	{
		return inKeywordBox.getTokens();
	}
	public void dispose()
	{
		final long f1 = Runtime.getRuntime().freeMemory();
		final long t1 = Runtime.getRuntime().totalMemory();
		final long m1 = Runtime.getRuntime().maxMemory();
//		System.out.println("BEFORE: " + f1 + "\t" + m1 + "\t" + t1);
		System.gc();
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				long f2 = Runtime.getRuntime().freeMemory();
				long t2 = Runtime.getRuntime().totalMemory();
				long m2 = Runtime.getRuntime().maxMemory();
				long f3 = (f2-f1) / 1024 / 1024;
				long t3 = (t2-t1) / 1024 / 1024;
				long m3 = (m2-m1) / 1024 / 1024;
//				System.out.println("AFTER: " + f2 + "\t" + m2 + "\t" + t2);
//				System.out.println("DELTA: " + f3 + "\t" + m3 + "\t" + t3);
			}
		});
	}
}
