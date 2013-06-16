/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;

import org.korsakow.ide.ui.resources.EventEditor;

public class IntegerRangeConfig implements ArgConfig
{
	private boolean isEditable;
	private String label;
	private ListCellRenderer renderer;
	private Collection<Integer> choices;
	public IntegerRangeConfig(int min, int max, boolean isEditable, String label)
	{
		this(min, max, isEditable, label, new DefaultListCellRenderer());
	}
	public IntegerRangeConfig(int min, int max, boolean isEditable, String label, ListCellRenderer renderer)
	{
		this.label = label;
		this.renderer = renderer;
		this.isEditable = isEditable;
		choices = new ArrayList<Integer>();
		for (int i = min; i <= max; ++i)
			choices.add(i);
	}
	public String getLabel() {
		return label;
	}
	public Collection<?> getChoices() {
		return choices;
	}
	public boolean isEditable() {
		return isEditable;
	}
	public ListCellRenderer getRenderer(EventEditor editor) {
		return renderer;
	}
}