/**
 * 
 */
package org.korsakow.ide.ui.controller.action.helper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.ui.dialogs.ProgressDialog;

public class ProgressDialogWorkerListener implements PropertyChangeListener
{
	ProgressDialog progressDialog;
	public ProgressDialogWorkerListener(ProgressDialog progressDialog)
	{
		this.progressDialog = progressDialog;
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(IWorker.PROPERTY_PROGRESS)) {
			progressDialog.setMainProgress((Integer)event.getNewValue());
			progressDialog.setSubProgress(0);
		} else
		if (event.getPropertyName().equals(IWorker.PROPERTY_DISPLAY_STRING)) {
			progressDialog.setMainDisplayString((String)event.getNewValue());
			progressDialog.setSubDisplayString("");
		} else
		if (event.getPropertyName().equals(IWorker.PROPERTY_SUB_PROGRESS)) {
			progressDialog.setSubProgress((Integer)event.getNewValue());
		} else
		if (event.getPropertyName().equals(IWorker.PROPERTY_SUB_DISPLAY_STRING)) {
			progressDialog.setSubDisplayString((String)event.getNewValue());
		}
	}
}