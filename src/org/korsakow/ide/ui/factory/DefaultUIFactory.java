package org.korsakow.ide.ui.factory;

import java.awt.Component;
import java.awt.LayoutManager;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.BoundedRangeModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.UIResourceManager;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class DefaultUIFactory implements IUIFactory
{
	private final HashMap<String, LayoutManager> layoutMap = new HashMap<String, LayoutManager>();
	
	public LayoutManager createLayout(String name)
	{
		if (!layoutMap.containsKey(name)) {
			try {
				Document document = UIResourceManager.getLayout(name);
				DomLayout layout = new DomLayout(document);
				layoutMap.put(name, layout);
			} catch (IOException e) {
				Logger.getLogger(DefaultUIFactory.class).error("", e);
				throw new RuntimeException(e);
			} catch (SAXException e) {
				Logger.getLogger(DefaultUIFactory.class).error("", e);
				throw new RuntimeException(e);
			} catch (ParserConfigurationException e) {
				Logger.getLogger(DefaultUIFactory.class).error("", e);
				throw new RuntimeException(e);
			}
		}
		return layoutMap.get(name);
	}
	
	/**
	 * Everything goes through here.
	 * @param comp
	 */
	private void commonHandler(String name, Component comp)
	{
		comp.setName(name);
	}
	
	public JLabel createLabel(String name)
	{
		return createLabel(name, "");
	}
	public JLabel createLabel(String name, String text)
	{
		return createLabel(name, text, null);
	}
		public JLabel createLabel(String name, Icon icon)
	{
		return createLabel(name, "", icon);
	}
	public JLabel createLabel(String name, String text, Icon icon)
	{
		JLabel label = new JLabel(text, icon, SwingConstants.LEFT);
		commonHandler(name, label);
		return label;
	}
	
	
	public JTextArea createTextArea(String name)
	{
		JTextArea area = new JTextArea();
		commonHandler(name, area);
		return area;
	}
	
	
	public JTextField createTextField(String name)
	{
		JTextField field = new JTextField();
		commonHandler(name, field);
		return field;
	}
	public JTextField createTextField(String name, String text)
	{
		JTextField field = new JTextField(text);
		commonHandler(name, field);
		return field;
	}
	
	public JCheckBox createCheckBox(String name)
	{
		return createCheckBox(name, "");
	}
	public JCheckBox createCheckBox(String name, String label)
	{
		JCheckBox check = new JCheckBox(label);
		commonHandler(name, check);
		return check;
	}
	
	
	public JButton createButton(String name)
	{
		JButton button = new JButton();
		commonHandler(name, button);
		return button;
	}
	public JButton createButton(String name, String label)
	{
		JButton button = new JButton(label);
		commonHandler(name, button);
		return button;
	}
	public JButton createButton(String name, Icon icon) {
		return createButton(name, "", icon);
	}
	public JButton createButton(String name, String label, Icon icon) {
		JButton button = new JButton(label, icon);
		commonHandler(name, button);
		return button;
	}
	
	public JToggleButton createToggleButton(String name, String label, Icon icon, boolean selected) {
		JToggleButton button = new JToggleButton(label, icon, selected);
		commonHandler(name, button);
		return button;
	}
	
	public JRadioButton createRadioButton(String name)
	{
		JRadioButton button = new JRadioButton();
		commonHandler(name, button);
		return button;
	}
	public JRadioButton createRadioButton(String name, String label)
	{
		JRadioButton button = new JRadioButton(label);
		commonHandler(name, button);
		return button;
	}
	public JRadioButton createRadioButton(String name, Icon icon) {
		return createRadioButton(name, "", icon);
	}
	public JRadioButton createRadioButton(String name, String label, Icon icon) {
		JRadioButton button = new JRadioButton(label, icon);
		commonHandler(name, button);
		return button;
	}
	
	public JComboBox createComboBox(String name)
	{
		return createComboBox(name, new DefaultComboBoxModel());
	}
	public JComboBox createComboBox(String name, ComboBoxModel model)
	{
		return createComboBox(name, model, new DefaultListCellRenderer());
	}
	public JComboBox createComboBox(String name, ComboBoxModel model, ListCellRenderer renderer)
	{
		JComboBox combo = new JComboBox();
		combo.setModel(model);
		combo.setRenderer(renderer);
		commonHandler(name, combo);
		return combo;
	}
	
	
	public JList createList(String name)
	{
		return createList(name, new DefaultListModel());
	}
	public JList createList(String name, ListModel model)
	{
		JList list = new JList();
		list.setModel(model);
		commonHandler(name, list);
		return list;
	}

	
	public JTabbedPane createTabbedPane(String name)
	{
		JTabbedPane pane = new JTabbedPane();
		commonHandler(name, pane);
		return pane;
	}
	
	public JSlider createHorizontalSlider(String name)
	{
		return createHorizontalSlider(name, 0, 100, 50); // same defaults as JSlider
	}
	public JSlider createHorizontalSlider(String name, int min, int max, int initial)
	{
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, initial);
		commonHandler(name, slider);
		return slider;
	}
	
	public JSplitPane createSplitPane(String name)
	{
		JSplitPane pane = new JSplitPane();
		commonHandler(name, pane);
		return pane;
	}
	
	public JSlider createSlider(String name, BoundedRangeModel model)
	{
		JSlider slider = new JSlider(model);
		commonHandler(name, slider);
		return slider;
	}
	
	public <T extends Component> T customComponent(String name, T comp)
	{
		commonHandler(name, comp);
		return comp;
	}
}
