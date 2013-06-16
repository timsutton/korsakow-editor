/**
 * 
 */
package org.korsakow.ide.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JLabel;

import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

public class ResourceIcon extends JLabel
{
	public static enum Action
	{
		Activated
	}
	public ResourceIcon()
	{
		setFocusable(true);
		setOpaque(true);
		initListeners();
		clear();
	}
	private void fireActionPerformed(String action)
	{
		UIUtil.dispatchEvent(listenerList, new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action));
	}
	public void initListeners()
	{
	}
	public void clear()
	{
		setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_MISSING));
		setText("");
	}
	public void setResource(Icon icon, String name)
	{
		setIcon(icon);
		setText(name);
	}
	public void addActionListener(ActionListener listener)
	{
		listenerList.add(ActionListener.class, listener);
	}
}