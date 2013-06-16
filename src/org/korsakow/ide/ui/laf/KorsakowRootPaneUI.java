package org.korsakow.ide.ui.laf;

import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.text.DefaultEditorKit;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.Util;

public class KorsakowRootPaneUI extends BasicRootPaneUI
{
	/**
	 * Simulates the WINDOW_CLOSING event.
	 * This is pertinant since it allows JFrame/JDialog defaultCloseOperation and the event hooks to operate as usual.
	 * @author d
	 *
	 */
	private static class CloseWindowAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source instanceof JRootPane) {
				JRootPane rootPane = (JRootPane)source;
				Object topLevel = rootPane.getTopLevelAncestor();
				if (topLevel instanceof Window) {
					Window window = (Window)topLevel;
					WindowEvent event = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
					Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
				}
			}
		}
	}
	private static Action closeWindowAction;
	private static Action getDisposeWindowAction()
	{
		if (closeWindowAction == null)
			closeWindowAction = new CloseWindowAction();
		return closeWindowAction;
	}
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowRootPaneUI();
    }  
    
    public void installUI(JComponent c) {
    	super.installUI(c);
    	switch (Platform.getOS())
    	{
    	case WIN:
    		// apparantly this is not needed as it is provided by the platform in this case
//    		c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK), "closeWindow");
			break;
    	case MAC:
    		c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.META_MASK), "closeWindow");
			break;
		default:
			Logger.getLogger(KorsakowRootPaneUI.class).error("No window close binding", new Throwable());
			break;
    	}
		c.getActionMap().put("closeWindow", getDisposeWindowAction());
    }
}
