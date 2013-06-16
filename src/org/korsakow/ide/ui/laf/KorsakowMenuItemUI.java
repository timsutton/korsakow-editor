package org.korsakow.ide.ui.laf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

public class KorsakowMenuItemUI extends BasicMenuItemUI
{
    public static ComponentUI createUI(JComponent c) 
    {
    	return new KorsakowMenuItemUI();
    }
//    public void paint(Graphics g, JComponent c) {
//    	super.paintBackground(g, c);
//    }

}
