package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SnuTDG
{
	public static final String NODE_NAME = "Snu";
	
	public static int insert(long version,
			String name, Long mainMediaId, float rating,
			Long backgroundSoundId, String backgroundSoundMode, float backgroundSoundVolume, boolean backgroundSoundLooping,
			Long interfaceId,
			Long lives, boolean looping, Long max_links, boolean starter, boolean ender,
			Long previewMediaId, String previewText,
			String insertText) throws XPathException{
		return insert(DataRegistry.getMaxId(), version, name, mainMediaId, rating, backgroundSoundId, backgroundSoundMode, backgroundSoundVolume, backgroundSoundLooping, interfaceId, lives, looping, max_links, starter, ender, previewMediaId, previewText, insertText);
	}
	public static int insert(long id, long version,
			String name, Long mainMediaId, float rating,
			Long backgroundSoundId, String backgroundSoundMode, float backgroundSoundVolume, boolean backgroundSoundLooping,
			Long interfaceId,
			Long lives, boolean looping, Long max_links, boolean starter, boolean ender,
			Long previewMediaId, String previewText,
			String insertText) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().appendElement("/korsakow/snus", NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version, name, mainMediaId, rating, backgroundSoundId, backgroundSoundMode, backgroundSoundVolume, backgroundSoundLooping, interfaceId, lives, looping, max_links, starter, ender, previewMediaId, previewText, insertText);
	}

	public static int delete(long id, long version) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/snus/Snu[id=? and version=?]", id, version));
		return list.getLength();
	}
	
	public static int update(long id, long version,
			String name, Long mainMediaId, float rating,
			Long backgroundSoundId, String backgroundSoundMode, float backgroundSoundVolume, boolean backgroundSoundLooping,
			Long interfaceId,
			Long lives, boolean looping, Long max_links, boolean starter, boolean ender,
			Long previewMediaId, String previewText,
			String insertText) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/snus/Snu[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		DataRegistry.getHelper().setString(element, "name", name);
		DataRegistry.getHelper().setLong(element, "mainMediaId", mainMediaId);
		DataRegistry.getHelper().setFloat(element, "rating", rating);
		DataRegistry.getHelper().setLong(element, "backgroundSoundId", backgroundSoundId);
		DataRegistry.getHelper().setString(element, "backgroundSoundMode", backgroundSoundMode);
		DataRegistry.getHelper().setFloat(element, "backgroundSoundVolume", backgroundSoundVolume);
		DataRegistry.getHelper().setBoolean(element, "backgroundSoundLooping", backgroundSoundLooping);
		DataRegistry.getHelper().setLong(element, "interfaceId", interfaceId);
		DataRegistry.getHelper().setLong(element, "lives", lives);
		DataRegistry.getHelper().setBoolean(element, "looping", looping);
		DataRegistry.getHelper().setLong(element, "maxLinks", max_links);
		DataRegistry.getHelper().setBoolean(element, "starter", starter);
		DataRegistry.getHelper().setBoolean(element, "ender", ender);
		DataRegistry.getHelper().setLong(element, "previewMediaId", previewMediaId);
		DataRegistry.getHelper().setString(element, "previewText", previewText);
		DataRegistry.getHelper().setString(element, "insertText", insertText);
		return 1;
	}
	
	public static void  createSnuTable() throws XPathExpressionException {
		if (DataRegistry.getHelper().xpathAsElement("/korsakow/snus") == null) {
			DataRegistry.getHelper().appendElement("/korsakow", "snus");
		}
	}
	public static void dropTable () throws XPathExpressionException{
		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/snus"));
	}
}
