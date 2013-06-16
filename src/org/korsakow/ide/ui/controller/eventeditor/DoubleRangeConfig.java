/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ListCellRenderer;

import org.korsakow.ide.ui.resources.EventEditor;

public class DoubleRangeConfig implements ArgConfig
{
	private boolean isEditable;
	private String label;
	private ListCellRenderer renderer;
	private Collection<Double> choices;
	public DoubleRangeConfig(double min, double max, int steps, boolean isEditable, String label)
	{
		this(min, max, steps, isEditable, label, new DefaultListCellRenderer());
	}
	public DoubleRangeConfig(double min, double max, int steps, boolean isEditable, String label, ListCellRenderer renderer)
	{
		this.label = label;
		this.renderer = renderer;
		this.isEditable = isEditable;
		choices = new ArrayList<Double>();
		double inc = (max-min)/(double)steps;
		for (int i = 0; i < steps; ++i)
			choices.add(min + inc*i);
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