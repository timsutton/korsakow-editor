package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class InterfaceTDG
{
	public static final String NODE_NAME = "Interface";
	public static int insert(long version, String name, int grid_width, int grid_height, Integer view_width, Integer view_height, Long click_sound_id, float click_sound_volume, Long background_image_id, String background_color) throws XPathException{
		return insert(DataRegistry.getMaxId(), version, name, grid_width, grid_height, view_width, view_height, click_sound_id, click_sound_volume, background_image_id, background_color);
	}
	public static int insert(long id, long version, String name, int grid_width, int grid_height, Integer view_width, Integer view_height, Long click_sound_id, float click_sound_volume, Long background_image_id, String background_color) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		if (element != null)
			throw new XPathExpressionException("an interface with that id already exists: " + id);
		element = DataRegistry.getHelper().appendElement("/korsakow/interfaces", NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version, name, grid_width, grid_height, view_width, view_height, click_sound_id, click_sound_volume, background_image_id, background_color);
	}

	public static int delete(long id, long version) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=? and version=?]", id, version));
		return list.getLength();
	}
	
	public static int update(
			long id, long version, 
			String name, 
			int grid_width, int grid_height, 
			Integer view_width, Integer view_height, 
			Long click_sound_id, float click_sound_volume, 
			Long background_image_id, String background_color
		) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		DataRegistry.getHelper().setString(element, "name", name);
		DataRegistry.getHelper().setInt(element, "gridWidth", grid_width);
		DataRegistry.getHelper().setInt(element, "gridHeight", grid_height);
		DataRegistry.getHelper().setInt(element, "viewWidth", view_width);
		DataRegistry.getHelper().setInt(element, "viewHeight", view_height);
		DataRegistry.getHelper().setLong(element, "clickSoundId", click_sound_id);
		DataRegistry.getHelper().setFloat(element, "clickSoundVolume", click_sound_volume);
		DataRegistry.getHelper().setLong(element, "backgroundImageId", background_image_id);
		DataRegistry.getHelper().setString(element, "backgroundColor", background_color);
		return 1;
	}
	
	public static Element createInterfaceTable() throws XPathExpressionException {
		Element element = null;
		if ((element = DataRegistry.getHelper().xpathAsElement("/korsakow/interfaces")) == null) {
			element = DataRegistry.getHelper().appendElement("/korsakow", "interfaces");
		}
		return element;
	}
	public static void dropTable () throws XPathExpressionException{
		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/interfaces"));
	}
}
