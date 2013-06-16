package org.korsakow.ide.ui.dnd;

import java.awt.datatransfer.DataFlavor;

import org.korsakow.ide.ui.components.tree.KNode;

public class DataFlavors
{
	public static class VideoFlavorClass {}
	public static class SoundFlavorClass {}
	public static class ImageFlavorClass {}
	public static class TextFlavorClass {}
	public static class SnuFlavorClass {}
	public static class WidgetTypeFlavorClass {}
	
	private static final DataFlavor createListFlavor(Class collectionClass, Class itemClass, String name)
	{
		return new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "-korsakow-"+name+";collectionClass="+collectionClass.getCanonicalName()+";itemClass="+itemClass.getCanonicalName(), name);
	}
	public static final DataFlavor VideoFlavor = new DataFlavor(VideoFlavorClass.class, "Video");
	public static final DataFlavor SoundFlavor = new DataFlavor(SoundFlavorClass.class, "Sound");
	public static final DataFlavor ImageFlavor = new DataFlavor(ImageFlavorClass.class, "Image");
	public static final DataFlavor TextFlavor = new DataFlavor(TextFlavorClass.class, "Text");
	public static final DataFlavor SnuFlavor = new DataFlavor(SnuFlavorClass.class, "SNU");
	public static final DataFlavor WidgetType = new DataFlavor(WidgetTypeFlavorClass.class, "Widget");
	public static final DataFlavor TreeTableNodesFlavor = new DataFlavor(KNode.class, "TreePath");
}

