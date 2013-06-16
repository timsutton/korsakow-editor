/**
 * 
 */
package org.korsakow.ide.resources.widget.propertyhandler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.colorchooser.AbstractColorChooserPanel;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.services.util.ColorFactory;

public class ColorPropertyHandler extends DefaultPropertyHandler
{
	private static String toString(Color value)
	{
		if (value == null) {
			return null;
		} else {
			return ColorFactory.formatCSS(value);
		}
	}
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, final JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
		editor.setRenderer(this);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(new String[]{}, value));
		
		final JColorChooser chooser = createColorChooser(Color.red);
		
		ActionListener okListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				editor.getModel().setSelectedItem(ColorPropertyHandler.toString(chooser.getColor()));
				notifyEditingStopped();
			}
		};
		ActionListener cancelListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				notifyEditingCanceled();
			}
		};
		final JDialog dialog = JColorChooser.createDialog(editor, "Color", true, chooser, okListener, cancelListener);
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				dialog.setVisible(true);
			}
		});
	}
	public static JColorChooser createColorChooser(Color initial)
	{
		final JColorChooser chooser = new JColorChooser(initial);
		AbstractColorChooserPanel defaultPanels[] = chooser.getChooserPanels();
		if (defaultPanels.length == 3) // remove other useless panels
			defaultPanels = new AbstractColorChooserPanel[]{defaultPanels[1]}; // [1] should be the HSV chooser
		chooser.setChooserPanels(defaultPanels);
		return chooser;
	}
	@Override
	public Component getPropertyRenderer(String propertyName, Object propertyValue)
	{
//		System.out.println(propertyName + "\t" + propertyValue);
		return getListCellRendererComponent(null, propertyValue, -1, false, false);
	}
	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected,
			boolean cellHasFocus)
	{
		String entry = (String)value;
		if (entry != null) {
			Color color = ColorFactory.createRGB(entry);
			setBackground(color);
			setText("");
			setOpaque(true);
		} else {
			setOpaque(false);
			setBackground(null);
			setForeground(null);
			setText("--");
		}
		setPreferredSize(new Dimension(70, 20));
		return this;
	}
}
