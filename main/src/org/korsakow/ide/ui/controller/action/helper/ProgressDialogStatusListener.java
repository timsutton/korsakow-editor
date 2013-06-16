package org.korsakow.ide.ui.controller.action.helper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.task.UIWorker;

import com.sun.swingx.SwingWorker.StateValue;

public class ProgressDialogStatusListener implements PropertyChangeListener
{

	protected final JDialog progressDialog;

	public ProgressDialogStatusListener(JDialog progressDialog)
	{
		this.progressDialog = progressDialog;
	}

	protected void handleException(Throwable e)
	{
		Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantexport.title"), e);
	}

	protected void onDone()
	{
	}

	public void propertyChange(PropertyChangeEvent event)
	{
		if (event.getNewValue() != StateValue.DONE)
			return;
		UIWorker worker = (UIWorker)event.getSource();
		Throwable exception = worker.getException();
		progressDialog.dispose();
		if (exception != null) {
			handleException(exception);
		} else {
			if (!worker.isCancelled())
				onDone();
		}
	}

}