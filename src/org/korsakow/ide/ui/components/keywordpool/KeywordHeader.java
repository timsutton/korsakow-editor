/**
 * 
 */
package org.korsakow.ide.ui.components.keywordpool;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.pool.AbstractHeader;
import org.korsakow.ide.ui.components.pool.ActionLabel;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;

public class KeywordHeader extends AbstractHeader
{
	public static final String OUT_ACTION = "out";
	public static final String IN_ACTION = "in";
	public static final String ITEM_ACTION = "item";
	protected ActionLabel inLabel;
	protected ActionLabel outLabel;
	protected ActionLabel itemLabel;

	public KeywordHeader(String inText, String outText, String itemText) {
		initUI(inText, outText, itemText);

		itemLabel.setColor("activeForeground", new Color(0xd400fd));
		outLabel.setColor("activeForeground", Color.blue);
		inLabel.setColor("activeForeground", Color.red);
		
		inLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		outLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		itemLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
	}
	public void initUI(String inText, String outText, String itemText)
	{
		putClientProperty("roundedCorners", false);
		setLayout(new FlowLayout(FlowLayout.LEFT));
		IUIFactory uifac = UIFactory.getFactory();
		add(inLabel = uifac.customComponent("inLabel", new ActionLabel()));
		add(outLabel = uifac.customComponent("outLabel", new ActionLabel()));
		add(Box.createHorizontalStrut(10));
		add(itemLabel = uifac.customComponent("keywordLabel", new ActionLabel()));
		Dimension squareSize = new Dimension(18, 18);
//		inLabel.setMaximumSize(squareSize);
		inLabel.setPreferredSize(squareSize);
//		outLabel.setMaximumSize(squareSize);
		outLabel.setPreferredSize(squareSize);
		
//		inLabel.setColor("background", Color.blue.darker());
//		inLabel.setColor("activeBackground", Color.blue);
//		inLabel.setColor("foreground", Color.white);
//		inLabel.setColor("activeForeground", Color.white);
//		
//		outLabel.setColor("background", Color.red.darker());
//		outLabel.setColor("activeBackground", Color.red);
//		outLabel.setForeground(Color.white);
//		outLabel.setColor("foreground", Color.white);
//		outLabel.setColor("activeForeground", Color.white);
		
		inLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inLabel.setVerticalAlignment(SwingConstants.CENTER);
//		inLabel.setOpaque(true);
		outLabel.setHorizontalAlignment(SwingConstants.CENTER);
		outLabel.setVerticalAlignment(SwingConstants.CENTER);
//		outLabel.setOpaque(true);
		
		inLabel.setActionCommand(IN_ACTION);
		outLabel.setActionCommand(OUT_ACTION);
		itemLabel.setActionCommand(ITEM_ACTION);
		
		inLabel.setToolTipText(LanguageBundle.getString("pool.inkeyword.tooltip"));
		outLabel.setToolTipText(LanguageBundle.getString("pool.outkeyword.tooltip"));
//		inLabel.setVisible(false);
//		outLabel.setVisible(false);
//		keywordLabel.setVisible(false);
		
		inLabel.addMouseListener(new Repainter());
		outLabel.addMouseListener(new Repainter());
		itemLabel.addMouseListener(new Repainter());
		
		setLayout(new FlowLayout(FlowLayout.LEFT, 0,0));
		
		setInText(inText);
		setOutText(outText);
		setItemText(itemText);
		setAlignmentY(0);
	}
	public void setInVisible(boolean visible)
	{
		inLabel.setVisible(visible);
	}
	public void setOutVisible(boolean visible)
	{
		outLabel.setVisible(visible);
	}
	public void setInText(String text)
	{
		inLabel.setText(text);
	}
	public void setOutText(String text)
	{
		outLabel.setText(text);
	}
	public void setItemText(String text)
	{
		itemLabel.setText(text);
	}
	public String getItemText()
	{
		return itemLabel.getText();
	}
	public void addItemActionListener(ActionListener listener)
	{
		itemLabel.addActionListener(listener);
	}
	public void addInActionListener(ActionListener listener)
	{
		inLabel.addActionListener(listener);
	}
	public void addOutActionListener(ActionListener listener)
	{
		outLabel.addActionListener(listener);
	}
	private class Repainter extends MouseAdapter
	{
		public void mouseEntered(MouseEvent event)
		{
			repaint();
		}
		public void mouseExited(MouseEvent event)
		{
			repaint();
		}
	}
}