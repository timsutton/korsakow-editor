/**
 * 
 */
package org.korsakow.ide.ui.laf;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class KFileChooserUI extends BasicFileChooserUI
{
    public static ComponentUI createUI(JComponent c) 
    {
    	return new KFileChooserUI((JFileChooser)c);
    }
	public KFileChooserUI(JFileChooser b) {
		super(b);
		
	}
	
}