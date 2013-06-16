package org.korsakow.services.tdg;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.Build;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ProjectTDG
{
	public static final String NODE_NAME = "Project";
	
	public static int insert(long id, long version, String name, Long settingsId,
			int movieWidth, int movieHeight,
			Long backgroundSoundId, float backgroundSoundVolume, boolean backgroundSoundLooping,
			Long clickSoundId, float clickSoundVolume,
			Long backgroundImageId, String backgroundColor,
			Long splashScreenMediaId,
			boolean randomLinkMode, boolean keepLinks,
			Long maxLinks,
			Long defaultInterface,
			String uuid) throws XPathExpressionException {
		Element element = DataRegistry.getHelper().appendElement("/korsakow/projects", NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version, name, settingsId,
				movieWidth, movieHeight,
				backgroundSoundId, backgroundSoundVolume, backgroundSoundLooping,
				clickSoundId, clickSoundVolume,
				backgroundImageId, backgroundColor,
				splashScreenMediaId,
				randomLinkMode, keepLinks,
				maxLinks,
				defaultInterface,
				uuid);
	}

	public static int delete(long id, long version) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/projects/Project[id=? and version=?]", id, version));
		return list.getLength();
	}
	
	public static int update(long id, long version, String name, Long settingsId,
			int movieWidth, int movieHeight,
			Long backgroundSoundId, float backgroundSoundVolume, boolean backgroundSoundLooping,
			Long clickSoundId, float clickSoundVolume,
			Long backgroundImageId, String backgroundColor,
			Long splashScreenMediaId,
			boolean randomLinkMode, boolean keepLinks,
			Long maxLinks,
			Long defaultInterface,
			String uuid) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/projects/Project[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		DataRegistry.getHelper().setString(element, "name", name!=null?name:"");
		DataRegistry.getHelper().setLong(element, "settingsId", settingsId);
		DataRegistry.getHelper().setInt(element, "movieWidth", movieWidth);
		DataRegistry.getHelper().setInt(element, "movieHeight", movieHeight);
		DataRegistry.getHelper().setLong(element, "backgroundSoundId", backgroundSoundId);
		DataRegistry.getHelper().setFloat(element, "backgroundSoundVolume", backgroundSoundVolume);
		DataRegistry.getHelper().setBoolean(element, "backgroundSoundLooping", backgroundSoundLooping);
		DataRegistry.getHelper().setLong(element, "clickSoundId", clickSoundId);
		DataRegistry.getHelper().setFloat(element, "clickSoundVolume", clickSoundVolume);
		DataRegistry.getHelper().setLong(element, "backgroundImageId", backgroundImageId);
		DataRegistry.getHelper().setString(element, "backgroundColor", backgroundColor);
		DataRegistry.getHelper().setLong(element, "splashScreenMediaId", splashScreenMediaId);
		DataRegistry.getHelper().setBoolean(element, "randomLinkMode", randomLinkMode);
		DataRegistry.getHelper().setBoolean(element, "keepLinksOnEmptySearch", keepLinks);
		DataRegistry.getHelper().setLong(element, "maxLinks", maxLinks);
		DataRegistry.getHelper().setLong(element, "defaultInterface", defaultInterface);
		DataRegistry.getHelper().setString(element, "uuid", uuid);
		
		DataRegistry.getHelper().getDocument().getDocumentElement().setAttribute("versionMajor", Build.getVersion());
		DataRegistry.getHelper().getDocument().getDocumentElement().setAttribute("versionMinor", ""+Build.getRelease());
		return 1;
	}
	
	public static void  createProjectTable() throws XPathExpressionException {
		if (DataRegistry.getHelper().xpathAsElement("/korsakow/projects") == null) {
			DataRegistry.getHelper().appendElement("/korsakow", "projects");
		}
	}
	public static void dropTable () throws XPathExpressionException{
		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/projects"));
	}
}
