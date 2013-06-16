package org.korsakow.ide.ui.dnd;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public abstract class AggregateTransferHandler extends TransferHandler
{
	protected List<TransferHandler> handlers = new ArrayList<TransferHandler>();
	/**
	 * Override this to choose the handler which will import the transfer.
	 * @return null if the transfer should not be handled at all.
	 */
	protected abstract TransferHandler pickTransferHandler(TransferSupport support);
	
	public AggregateTransferHandler()
	{
		
	}
	
	@Override
	public int getSourceActions(JComponent comp)
	{
		return TransferHandler.COPY_OR_MOVE;
	}
	
	public void addHandler(TransferHandler handler)
	{
		handlers.add(handler);
	}
	@Override
	public boolean canImport(TransferSupport support)
	{
		for (TransferHandler handler : handlers)
			if (handler.canImport(support))
				return true;
		return false;
	}

	@Override
	public boolean importData(TransferSupport support) {
		TransferHandler handler = pickTransferHandler(support);
		if (handler != null)
			return handler.importData(support);
		return false;
	}

}
