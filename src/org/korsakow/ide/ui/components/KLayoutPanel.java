package org.korsakow.ide.ui.components;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * A panel that (unlike JPanel) doesn't draw, is used purely for layout purposes.
 * The intent is to make code easier to maintain than littering JPanel.setOpaque all over.
 * Also easier to manage colors.
 * @deprecated
 */
public class KLayoutPanel extends JPanel// implements Scrollable
{
	public KLayoutPanel()
	{
		super();
//		setOpaque(false);
	}
	public KLayoutPanel(LayoutManager layout)
	{
		super(layout);
//		setOpaque(false);
	}
//	public Dimension getPreferredScrollableViewportSize() {
//		return getPreferredSize();
//	}
//	public boolean getScrollableTracksViewportHeight() {
//		return false;
//	}
//	public boolean getScrollableTracksViewportWidth() {
//		return false;
//	}
//	public int getScrollableBlockIncrement(Rectangle visibleRect,
//			int orientation, int direction) {
//		return 10;
//	}
//	public int getScrollableUnitIncrement(Rectangle visibleRect,
//			int orientation, int direction) {
//		return 1;
//	}
}
