package org.korsakow.ide.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.korsakow.ide.ui.factory.UIFactory;

public class UIHelper
{
	public static JPanel createWestEastPanel(JComponent west, Component center, JComponent east)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(east, BorderLayout.EAST);
		panel.add(center, BorderLayout.CENTER);
		panel.add(west, BorderLayout.WEST);
		return panel;
	}
	public static JPanel createWestEastPanel(JComponent west, JComponent east)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(east, BorderLayout.EAST);
		panel.add(west, BorderLayout.WEST);
		return panel;
	}
	public static JPanel createHorizontalBoxLayoutPanel(Component... components)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		for (Component comp : components)
			panel.add(comp);
		return panel;
	}
	public static JPanel createVerticalBoxLayoutPanel(Component... components)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		for (Component comp : components)
			panel.add(comp);
		return panel;
	}
	public static JPanel createHorizontalFlowLayoutPanel(Component... components)
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		for (Component comp : components)
			panel.add(comp);
		return panel;
	}
	public static JPanel createHorizontalMarginPanel(JComponent content, int leftMargin, int rightMargin)
	{
		JPanel panel = new JPanel();
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(leftMargin));
		panel.add(content);
		panel.add(Box.createHorizontalStrut(rightMargin));
		return panel;
	}
	public static JPanel createLabelPanel(String label, JComponent content)
	{
		return createLabelPanel("", label, content);
	}
	public static JPanel createLabelPanel(String name, String label, JComponent content)
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setName("UIHelper.createLabelPanel");
		JLabel jlabel;
		panel.add(jlabel = UIFactory.getFactory().createLabel(name, label));
		jlabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(content);
		panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		return panel;
	}
	public static JPanel createBorderLayoutLabelPanel(String label, Component content)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel(label), BorderLayout.WEST);
		panel.add(content, BorderLayout.CENTER);
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		return panel;
	}
	public static JPanel createVerticalLabelPanel(String label, Component content)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel(label));
		panel.add(content);
		return panel;
	}
	public static JPanel createHorizontalStrut(int width) {
		JPanel panel = new JPanel();
		final Dimension d = new Dimension(width, 0);
		panel.setPreferredSize(d);
		panel.setMaximumSize(d);
		return panel;
	}
}
