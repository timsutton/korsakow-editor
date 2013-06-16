package org.korsakow.ide.ui.factory;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.BoundedRangeModel;
import javax.swing.ComboBoxModel;
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

public interface IUIFactory
{
	LayoutManager createLayout(String name);
	
	JLabel createLabel(String name);
	JLabel createLabel(String name, String label);
	JLabel createLabel(String name, Icon icon);
	JLabel createLabel(String name, String text, Icon icon);
	
	JTextArea createTextArea(String name);
	
	JTextField createTextField(String name);
	JTextField createTextField(String name, String text);
	
	JCheckBox createCheckBox(String name);
	JCheckBox createCheckBox(String name, String label);
	
	JButton createButton(String name);
	JButton createButton(String namej, String label);
	JButton createButton(String name, Icon icon);
	JButton createButton(String name, String label, Icon icon);
	
	JToggleButton createToggleButton(String name, String label, Icon icon, boolean selected);
	
	JRadioButton createRadioButton(String name);
	JRadioButton createRadioButton(String namej, String label);
	JRadioButton createRadioButton(String name, Icon icon);
	JRadioButton createRadioButton(String name, String label, Icon icon);
	
	JComboBox createComboBox(String name);
	JComboBox createComboBox(String name, ComboBoxModel model);
	JComboBox createComboBox(String name, ComboBoxModel model, ListCellRenderer renderer);
	
	JList createList(String name);
	JList createList(String name, ListModel model);
	
	JTabbedPane createTabbedPane(String name);
	
	JSlider createHorizontalSlider(String name);
	JSlider createHorizontalSlider(String name, int min, int max, int initial);
	
	JSplitPane createSplitPane(String name);
	
	JSlider createSlider(String name, BoundedRangeModel model);
	
	/**
	 * Not sure why, but it might have an idea to have any custom components go through here while we're at it.
	 * @param comp
	 * @return
	 */
	<T extends Component> T customComponent(String name, T comp);
}
