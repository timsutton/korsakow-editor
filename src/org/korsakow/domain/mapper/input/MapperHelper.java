package org.korsakow.domain.mapper.input;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Resource;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.proxy.ImageProxy;
import org.korsakow.domain.proxy.InterfaceProxy;
import org.korsakow.domain.proxy.ProjectProxy;
import org.korsakow.domain.proxy.RuleProxy;
import org.korsakow.domain.proxy.SnuProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.domain.proxy.TextProxy;
import org.korsakow.domain.proxy.VideoProxy;
import org.korsakow.domain.proxy.WidgetProxy;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MapperHelper
{
	public static final String[] REFERENCING_ELEMENTS = {
	                 				"backgroundSoundId",
	                				"backgroundImageId",
	                				"clickSoundId",
	                				"splashScreenMediaId",
	                				"soundId",
	                				"textId",
	                				"imageId",
	                				"mainMediaId",
	                				"videoId",
	                				"snuId",
	                				"mediaId",
	                				"startingSnuId",
	                				"interfaceId",
	                				"previewMediaId"
	};
	/**
	 * This is a terrible implementation.
	 * @param elm
	 * @return
	 * @throws MapperException
	 * @throws SQLException
	 */
	public static Resource mapUnknown(Element elm) throws MapperException, SQLException
	{
		long id = DomUtil.getLong(elm, "id");
		String tagName = elm.getTagName();
		if (tagName.equals("Snu"))
			return SnuInputMapper.map(id);
		if (tagName.equals("Video"))
			return VideoInputMapper.map(id);
		if (tagName.equals("Sound"))
			return SoundInputMapper.map(id);
		if (tagName.equals("Image"))
			return ImageInputMapper.map(id);
		if (tagName.equals("Project"))
			return ProjectInputMapper.map(id);
		if (tagName.equals("Rule"))
			return RuleInputMapper.map(id);
		if (tagName.equals("Widget"))
			return WidgetInputMapper.map(id);
		if (tagName.equals("Interface"))
			return InterfaceInputMapper.map(id);
		if (tagName.equals("Text"))
			return TextInputMapper.map(id);
		throw new MapperException("unknown resource type: " + tagName);
	}
	/**
	 * This is a terrible implementation.
	 * @param elm
	 * @return
	 * @throws MapperException
	 * @throws SQLException
	 */
	public static IResource proxyUnknown(Element elm) throws MapperException, SQLException
	{
		long id = DomUtil.getLong(elm, "id");
		String tagName = elm.getTagName();
		if (tagName.equals("Snu"))
			return new SnuProxy(id);
		if (tagName.equals("Video"))
			return new VideoProxy(id);
		if (tagName.equals("Sound"))
			return new SoundProxy(id);
		if (tagName.equals("Image"))
			return new ImageProxy(id);
		if (tagName.equals("Project"))
			return new ProjectProxy(id);
		if (tagName.equals("Rule"))
			return new RuleProxy(id);
		if (tagName.equals("Widget"))
			return new WidgetProxy(id);
		if (tagName.equals("Interface"))
			return new InterfaceProxy(id);
		if (tagName.equals("Text"))
			return new TextProxy(id);
//		if (tagName.equals("Settings"))
//			return new SettingsProxy(id);
		throw new MapperException("unknown resource type: " + tagName);
	}
	public static Collection<IResource> findResourcesReferencing(long id) throws MapperException, XPathExpressionException, SQLException
	{
		Set<IResource> references = new HashSet<IResource>();
		
		//		// TODO: obviously maintaining this list is not the way to go
//		// I would like to have it so the structure clearly differentiates between an ID declaration and
//		// a reference to an object's id
//		// something along the lines of <Snu><mainMedia><refId>1</refId></mainMedia></Snu>
		for (String ELEMENT : REFERENCING_ELEMENTS)
		{
			NodeList elements = DataRegistry.getDocument().getElementsByTagName(ELEMENT);
			int length = elements.getLength();
			for (int i = 0; i < length; ++i) {
				Element element = (Element)elements.item(i);
				long refId = Long.parseLong(element.getTextContent());
				if (refId == id)
					references.add(proxyUnknown((Element)element.getParentNode()));
			}
		}
		return references;
	}
}
