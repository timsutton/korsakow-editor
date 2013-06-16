/**
 * 
 */
package org.korsakow.ide.ui.controller.action.helper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.Application;

public final class CancelProgressDialogWindowAdapter extends
		WindowAdapter
{
	private final JDialog progressDialog;
	private final IWorker worker;

	public CancelProgressDialogWindowAdapter(JDialog progressDialog,
			IWorker worker)
	{
		this.progressDialog = progressDialog;
		this.worker = worker;
	}

	@Override
	public void windowClosing(WindowEvent windowEvent) {
		if (!Application.getInstance().showOKCancelDialog(progressDialog, "Are you sure?", "Do you really want to cancel the operation?")) {
			return;
		}
		windowEvent.getWindow().dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		if (!worker.isDone() && !worker.isCancelled())
			worker.cancel(true);
	}
}