package org.korsakow.ide.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

import org.korsakow.ide.resources.WidgetType;

public class WidgetTypeTransferable implements Transferable
{
	public static class IsAltDown{}
	public static final DataFlavor ALT_STATUS_FLAVOR = new DataFlavor(IsAltDown.class, "Is alt down");

	private final boolean isAltDown;
	private final WidgetType widgetType;
	public WidgetTypeTransferable(WidgetType widgetType, boolean isAltDown)
	{
		this.widgetType = widgetType;
		this.isAltDown = isAltDown;
	}
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor == DataFlavors.WidgetType)
			return widgetType;
		else
		if (flavor == ALT_STATUS_FLAVOR)
			return isAltDown;
		throw new UnsupportedFlavorException(flavor);
	}
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{DataFlavors.WidgetType, ALT_STATUS_FLAVOR};
	}
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return Arrays.asList(getTransferDataFlavors()).contains(flavor);
	}
}
