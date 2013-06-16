package org.korsakow.ide.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class AggregateFileTransferHandler extends TransferHandler
{
	public static interface FileTransferHandler
	{
		boolean importData(List<File> files);
	}
	protected List<FileTransferHandler> handlers = new ArrayList<FileTransferHandler>();
	public AggregateFileTransferHandler()
	{
		
	}
	@Override
	public int getSourceActions(JComponent comp)
	{
		return TransferHandler.COPY_OR_MOVE;
	}
	public void addHandler(FileTransferHandler handler)
	{
		handlers.add(handler);
	}
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] flavours)
	{
		if (!Arrays.asList(flavours).contains(DataFlavor.javaFileListFlavor))
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferSupport support)
	{
		if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			return false;
		List<File> files;
		try {
			files = (List<File>)support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
		} catch (UnsupportedFlavorException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		
		for (FileTransferHandler handler : handlers)
			if (handler.importData(files))
				return true;
		return false;
	}

}
