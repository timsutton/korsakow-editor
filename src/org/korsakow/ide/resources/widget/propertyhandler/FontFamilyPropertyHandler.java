/**
 * 
 */
package org.korsakow.ide.resources.widget.propertyhandler;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.WidgetModel;

public class FontFamilyPropertyHandler extends DefaultPropertyHandler
{
	private static final List<String> websafe = Arrays.asList( new String[] {
		"Arial",
		"Arial Black",
		"Century Gothic",
		"Comic Sans MS",
		"Courier New",
		"Garamond",
		"Georgia",
		"Impact",
		"Lucida Console",
		"Tahoma",
		"Times New Roman",
		"Trebuchet MS",
		"Verdana",
		"Webdings",
		"Wingdings",
	} );
	
	private static List<String> getFonts() {
		List<String> fonts = new ArrayList<String>( Arrays.asList( GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames() ) );
		fonts.retainAll( websafe );
		return fonts;
	}
	
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		editor.setEditable(false);
		List<String> model = getFonts();
		model.add(0, null);
		
		editor.setModel(new DefaultComboBoxModel(model.toArray()));
 		Object value = getCommonValue(widgets, propertyName);
		editor.getModel().setSelectedItem(value);
	}
}