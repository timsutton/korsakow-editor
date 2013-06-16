package org.korsakow.ide.ui.laf;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;


public class KorsakowListUI extends BasicListUI
{
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowListUI();
    }

    public void installUI(JComponent c)
    {
    	super.installUI(c);
    	c.putClientProperty("List.altBackground", UIManager.getColor("List.altBackground"));
    }
}
