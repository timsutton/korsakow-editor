/**
 * 
 */
package org.korsakow.ide.ui.components.snupool;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import org.korsakow.ide.ui.components.KList;
import org.korsakow.ide.ui.components.keywordpool.KeywordPool;
import org.korsakow.ide.ui.factory.IUIFactory;
import org.korsakow.ide.ui.factory.UIFactory;
import org.korsakow.ide.util.UIResourceManager;

public class KeywordCellRenderer extends JPanel implements ListCellRenderer
{
	private JLabel inLabel;
	private JLabel outLabel;
	private JLabel itemLabel;
	
	public static Icon blankIcon = UIResourceManager.getIcon(UIResourceManager.ICON_BLANK_SM);
	
	public KeywordCellRenderer()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
		IUIFactory uifac = UIFactory.getFactory();
		add(inLabel = uifac.createLabel("inLabel", blankIcon));
		add(outLabel = uifac.createLabel("outLabel", blankIcon));
		add(Box.createHorizontalStrut(10));
		add(itemLabel = uifac.createLabel("itemLabel"));
		Dimension squareSize = new Dimension(16, 16);
		inLabel.setMaximumSize(squareSize);
		inLabel.setPreferredSize(squareSize);
		outLabel.setMaximumSize(squareSize);
		outLabel.setPreferredSize(squareSize);
//		inLabel.setBackground(Color.blue);
//		inLabel.setForeground(Color.white);
//		outLabel.setBackground(Color.red);
//		outLabel.setForeground(Color.white);
		inLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inLabel.setVerticalAlignment(SwingConstants.CENTER);
//		inLabel.setOpaque(true);
		outLabel.setHorizontalAlignment(SwingConstants.CENTER);
		outLabel.setVerticalAlignment(SwingConstants.CENTER);
//		outLabel.setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 9));
	}
	public Component getListCellRendererComponent(
		       JList list,              // the list
		       Object value,            // value to display
		       int index,               // cell index
		       boolean isSelected,      // is the cell selected
		       boolean cellHasFocus)    // does the cell have focus
	{
		KeywordEntry entry = (KeywordEntry)value;
		
		inLabel.setIcon( ( entry.isInKeyword() ? KeywordPool.inIcon : blankIcon ) );
		outLabel.setIcon( ( entry.isOutKeyword() ? KeywordPool.outIcon : blankIcon ) );
		itemLabel.setText(entry.getKeyword());
		
		int mouseIndex = ((KList)list).getRolloverIndex();
		if (mouseIndex == index) {
			setBackground(UIManager.getColor("CollapsiblePaneHeader.background"));
		} else {
			setBackground(UIManager.getColor("CollapsiblePaneHeader.background2"));
		}
		return this;
	}
	
}