package org.korsakow.ide.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.korsakow.ide.ui.dnd.EmptyTransferable;
import org.korsakow.ide.ui.dnd.TransferableTreeTableNodes;

public class ClipboardHelper
{
	public static class ClipboardResult
	{
		private Transferable transferable;
		private int action;
		public ClipboardResult(Transferable transferable, int action)
		{
			this.transferable = transferable;
			this.action = action;
		}
		public Transferable getTransferable()
		{
			return transferable;
		}
		public int getAction()
		{
			return action;
		}
	}
	private static class CopyCutProxy implements Transferable
	{
		int action;
		private Transferable innerTransferable;
		
		public CopyCutProxy(int action, Transferable t)
		{
			this.action = action;
			this.innerTransferable = t;
		}
		
		public int getAction() {
			return action;
		}
		
		public Transferable getTransferable() {
			return innerTransferable;
		}

		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (CopyCutFlavor.match(flavor))
				return this;
			return innerTransferable.getTransferData(flavor);
		}

		public DataFlavor[] getTransferDataFlavors() {
			return innerTransferable.getTransferDataFlavors();
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			if (CopyCutFlavor.match(flavor))
				return true;
			return innerTransferable.isDataFlavorSupported(flavor);
		}
	}
	private static DataFlavor CopyCutFlavor = new DataFlavor(CopyCutProxy.class, "");
	
	public static void copy(Transferable t)
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferable = new CopyCutProxy(DnDConstants.ACTION_COPY, t);
		clipboard.setContents(transferable, null);
	}
	public static void cut(Transferable t)
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferable = new CopyCutProxy(DnDConstants.ACTION_MOVE, t);
		clipboard.setContents(transferable, null);
	}
	public static ClipboardResult paste()
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable t = clipboard.getContents(null);
		
		int action = DnDConstants.ACTION_COPY_OR_MOVE;
		if (t.isDataFlavorSupported(CopyCutFlavor)) {
			CopyCutProxy proxy = null;
			try {
				proxy = (CopyCutProxy)t.getTransferData(CopyCutFlavor);
				action = proxy.getAction();
				t = proxy.getTransferable();
			} catch (UnsupportedFlavorException e) { // this is one of those "will never happen" situations
				Logger.getLogger(ClipboardHelper.class).error("", e);
			} catch (IOException e) { // worse case they just get a general copy_or_move operation
				Logger.getLogger(ClipboardHelper.class).error("", e);
			}
		}
		
		if (action == DnDConstants.ACTION_MOVE) {
			clipboard.setContents(new EmptyTransferable(), null);
		}
		
		return new ClipboardResult(t, action);
	}
	
}
