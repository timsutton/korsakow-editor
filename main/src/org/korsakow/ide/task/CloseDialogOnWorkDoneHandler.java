/**
 * 
 */
package org.korsakow.ide.task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;

import com.sun.swingx.SwingWorker.StateValue;

public class CloseDialogOnWorkDoneHandler implements PropertyChangeListener {
	private JDialog dialog;
	public CloseDialogOnWorkDoneHandler(JDialog dialog)
	{
		this.dialog = dialog;
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getNewValue() == StateValue.DONE) {
			dialog.dispose();
		}
	}
}