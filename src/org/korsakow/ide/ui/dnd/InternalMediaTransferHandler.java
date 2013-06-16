/**
 * 
 */
package org.korsakow.ide.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;
import org.korsakow.domain.interf.IMedia;

/**
 * Typical usage is to simply override "importMedia(TransferSupport support, List<IMedia> media)"
 * @author d
 *
 * @param <IMedia>
 */
public abstract class InternalMediaTransferHandler extends TransferHandler
{
	public InternalMediaTransferHandler()
	{
	}
	protected boolean isInternalMediaObjectFlavor(DataFlavor flavor)
	{
		return (IMedia.class.isAssignableFrom(flavor.getRepresentationClass()));
	}
	public int getSourceActions(JComponent comp)
	{
		return TransferHandler.COPY_OR_MOVE;
	}
	/**
	 * Identical to canImport except it also returns the desired flavour.
	 * @param support
	 * @return null if no flavour matches
	 */
	protected DataFlavor pickImportFlavor(TransferSupport support)
	{
		DataFlavor[] flavors = support.getDataFlavors();
		for (DataFlavor flavor : flavors)
		{
			if (isInternalMediaObjectFlavor(flavor))
				return flavor;
		}
		return null;
	}
	
//	public boolean canImport(TransferHandler.TransferSupport support)
	/**
	 * Delegates to pickImportFlavor. It is convenient to override pickImportFlavor and not this method.
	 */
	@Override
	public boolean canImport(TransferSupport support)
	{
		return pickImportFlavor(support) != null;
	}

//	public boolean importData(TransferHandler.TransferSupport support) 
	@Override
	public boolean importData(TransferSupport support) 
	{
try {
		DataFlavor[] flavors = support.getDataFlavors();
		DataFlavor flavor = pickImportFlavor(support);
		assert flavor != null;
		Object data = null;
		try {
			data = support.getTransferable().getTransferData(flavor);
		} catch (UnsupportedFlavorException e) {
        	Logger.getLogger(InternalMediaTransferHandler.class).error("", e);
			return false;
		} catch (IOException e) {
        	Logger.getLogger(InternalMediaTransferHandler.class).error("", e);
			return false;
		}
		List<IMedia> objects;
		if (data instanceof List) {
			objects = (List<IMedia>) data;
		} else {
			objects = new ArrayList<IMedia>();
			objects.add((IMedia)data);
		}
		return importMedia(support, objects);
} catch (RuntimeException e) {
	Logger.getLogger(InternalMediaTransferHandler.class).error("", e);
	throw e;
}
	}
	/**
	 */
	protected boolean importMedia(TransferSupport support, List<? extends IMedia> media)
	{
		return false;
	}
}