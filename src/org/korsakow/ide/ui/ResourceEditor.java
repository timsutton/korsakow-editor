package org.korsakow.ide.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.KLayoutPanel;
import org.korsakow.ide.ui.resources.ResourceView;
import org.korsakow.ide.util.OrderedEventListenerList;
import org.korsakow.ide.util.UIResourceManager;


public class ResourceEditor extends JFrame
{
	//private DefaultResourceEditorController controller;
	private final OrderedEventListenerList listeners = new OrderedEventListenerList();
	private ResourceView resourceView;
	private KLayoutPanel resourcePanel;
	protected JButton saveButton;
	protected JButton saveCopyButton;
	protected Component saveCopySpacer;
	protected JButton cancelButton;
	
	public ResourceEditor()
	{
		//controller = new DefaultResourceEditorController(this);
		initUI();
		initListeners();
	}
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		setBackground(UIManager.getColor("window"));
//		getContentPane().setBackground(UIManager.getColor("window"));
		setLayout(new BorderLayout());
//		setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
//		setLayout(new FlowLayout(FlowLayout.LEFT));
		setIconImage(UIResourceManager.getImage(UIResourceManager.ICON_WINDOW_ICON));

		Box box;
		Box mainPanel = Box.createVerticalBox();
		add(mainPanel);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

		
		box = Box.createHorizontalBox();
		mainPanel.add(box);
		box.add(Box.createHorizontalGlue());
		
		resourcePanel = new KLayoutPanel();
		resourcePanel.setLayout(new BorderLayout());
		mainPanel.add(resourcePanel);
		
		mainPanel.add(Box.createVerticalGlue());
		
		box = Box.createHorizontalBox();
		mainPanel.add(box);
		box.add(Box.createHorizontalGlue());
		box.add(saveButton = new JButton(LanguageBundle.getString("resourceeditor.savebutton.label")));
		box.add(Box.createHorizontalStrut(15));
		box.add(saveCopyButton = new JButton(LanguageBundle.getString("resourceeditor.saveCopybutton.label")));
		saveCopyButton.setVisible( false );
		saveCopySpacer = box.add(Box.createHorizontalStrut(15));
		saveCopySpacer.setVisible( false );
		box.add(cancelButton = new JButton(LanguageBundle.getString("resourceeditor.cancelbutton.label")));
	}
	private void initListeners()
	{
		addWindowListener(new WindowAdapter() {
		    @Override
			public void windowClosed(WindowEvent e) {
		    	if (resourceView != null)
		    		resourceView.dispose();
		    }
		});
	}
	public void addSaveActionListener(ActionListener listener)
	{
		saveButton.addActionListener(listener);
	}
	public void addSaveCopyActionListener(ActionListener listener)
	{
		saveCopyButton.addActionListener(listener);
	}
	public void addCancelActionListener(ActionListener listener)
	{
		cancelButton.addActionListener(listener);
	}
	public void setSaveCopyVisible( boolean enabled ) {
		saveCopyButton.setVisible( enabled );
		saveCopySpacer.setVisible( enabled );
	}
	public void setResourceView(ResourceView resourceView, ResourceType resourceType)
	{
		if (this.resourceView != null)
			resourcePanel.remove(this.resourceView);
		this.resourceView = resourceView;
		resourcePanel.add(resourceView);
		setTitle(LanguageBundle.getString("resourceditor.window.title", resourceType.getDisplayString()));
	}
	public ResourceView getResourceView()
	{
		return resourceView;
	}
	public JButton getOKButton()
	{
		return saveButton;
	}
}
