/**
 * 
 */
package org.korsakow.ide.ui.components;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Window;

import javax.swing.JTextArea;
import javax.swing.UIManager;

public class KTooltip extends Window
{
	private JTextArea textArea;
	public KTooltip(Window owner)
	{
		super(owner);
		setBackground(UIManager.getColor("ToolTip.background"));
		setForeground(UIManager.getColor("ToolTip.foreground"));
		setLayout(new BorderLayout());
		textArea = new JTextArea();
		add(textArea);
	}
	public void setText(String text)
	{
		textArea.setText(text);
	}
	public void show(Point p, String text)
	{
		if (p != null)
			show(p.x, p.y, text);
	}
	public void show(int x, int y, String text)
	{
		setText(text);
		setLocation(x, y);
		pack();
		if (!isVisible()) // this check avoids focus wars on windows
			setVisible(true);
	}
}