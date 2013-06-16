/**
 * 
 */
package org.korsakow.ide.ui.components.pool;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;

import org.korsakow.ide.util.UIUtil;

/**
 * A label maskarading as a less functional button. Supports ActionEvents.
 * Uses an alternate method for setting its colors.
 * 
 * @author d
 *
 */
public class ActionLabel extends JLabel
{
	private String actionCommand = "activated";
	public ActionLabel()
	{
		setOpaque(false);
		
		setColor("foreground", getForeground());
		setColor("background", getBackground());
		setColor("activeForeground", getForeground());
		setColor("activeBackground", getBackground());
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event)
			{
				if (UIUtil.isRegularDoubleClick(event))
					fireActionPerformed(actionCommand);
			}
			public void mouseEntered(MouseEvent event)
			{
				setForeground(getColor("activeForeground"));
				setBackground(getColor("activeBackground"));
			}
			public void mouseExited(MouseEvent event)
			{
				setForeground(getColor("foreground"));
				setBackground(getColor("background"));
			}
		});
	}
	protected void fireActionPerformed(String actionCommand)
	{
		ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand);
		for (ActionListener listener : listenerList.getListeners(ActionListener.class))
		{
			listener.actionPerformed(event);
		}
	}
	public void setColor(String key, Color value)
	{
		putClientProperty(key, value);
		if (key.equals("background"))
			setBackground(value);
		if (key.equals("foreground"))
			setForeground(value);
		repaint();
	}
	public Color getColor(String key)
	{
		return (Color)getClientProperty(key);
	}
	public void setActionCommand(String actionCommand)
	{
		this.actionCommand = actionCommand;
	}
	public void addActionListener(ActionListener listener)
	{
		listenerList.add(ActionListener.class, listener);
	}
}