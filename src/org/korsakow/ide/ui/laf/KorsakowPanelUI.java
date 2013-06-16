package org.korsakow.ide.ui.laf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class KorsakowPanelUI extends BasicPanelUI
{
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowPanelUI();
    }  
    
    public void installUI(JComponent c) {
    	super.installUI(c);
//    	c.setOpaque(false);
    }
}
