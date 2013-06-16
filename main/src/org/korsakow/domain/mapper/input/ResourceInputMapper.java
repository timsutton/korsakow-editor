package org.korsakow.domain.mapper.input;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Image;
import org.korsakow.domain.Interface;
import org.korsakow.domain.Project;
import org.korsakow.domain.Rule;
import org.korsakow.domain.Snu;
import org.korsakow.domain.Sound;
import org.korsakow.domain.Text;
import org.korsakow.domain.Video;
import org.korsakow.domain.Widget;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.proxy.UnknownMediaProxy;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.finder.ResourceFinder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ResourceInputMapper
{
	public static IResource map(long id) throws MapperException{
		try {
			if ( IdentityMap.has(id, Snu.class) ) return IdentityMap.get(id, Snu.class);
			if ( IdentityMap.has(id, Video.class) ) return IdentityMap.get(id, Video.class);
			if ( IdentityMap.has(id, Sound.class) ) return IdentityMap.get(id, Sound.class);
			if ( IdentityMap.has(id, Image.class) ) return IdentityMap.get(id, Image.class);
			if ( IdentityMap.has(id, Text.class) ) return IdentityMap.get(id, Text.class);
			if ( IdentityMap.has(id, Interface.class) ) return IdentityMap.get(id, Interface.class);
			if ( IdentityMap.has(id, Widget.class) ) return IdentityMap.get(id, Widget.class);
			if ( IdentityMap.has(id, Rule.class) ) return IdentityMap.get(id, Rule.class);
			if ( IdentityMap.has(id, Project.class) ) return IdentityMap.get(id, Project.class);
			if ( IdentityMap.has(id, Snu.class) ) return IdentityMap.get(id, Snu.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e); // should not happen <- has()
		} catch (DomainObjectNotFoundException e) {
			throw new MapperException(e); // should not happen <- has()
		}
		try {
			NodeList list = ResourceFinder.find(id);
			if (list.getLength() == 0) {
				throw new DomainObjectNotFoundException("domain object not found: " + id);
			} else if (list.getLength() > 1)
				throw new MapperException("multiple objects found for: " + id);
			return MapperHelper.mapUnknown((Element)list.item(0));
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<IResource> findByClickSoundId(long id) throws MapperException {
		try {
			NodeList list = ResourceFinder.findByClickSoundId(id);
			List<IResource> resources = new ArrayList<IResource>();
			int length = list.getLength();
			for (int i = 0; i < length; ++i)
			{
				Element elm = (Element)list.item(i);
				resources.add(MapperHelper.proxyUnknown(elm));
			}
			return resources;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<IResource> findByBackgroundSoundId(long id) throws MapperException {
		try {
			NodeList list = ResourceFinder.findByBackgroundSoundId(id);
			List<IResource> resources = new ArrayList<IResource>();
			int length = list.getLength();
			for (int i = 0; i < length; ++i)
			{
				Element elm = (Element)list.item(i);
				resources.add(MapperHelper.proxyUnknown(elm));
			}
			return resources;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<IResource> findByPreviewMediaId(long id) throws MapperException {
		try {
			NodeList list = ResourceFinder.findByPreviewMediaId(id);
			List<IResource> resources = new ArrayList<IResource>();
			int length = list.getLength();
			for (int i = 0; i < length; ++i)
			{
				Element elm = (Element)list.item(i);
				resources.add(new UnknownMediaProxy(DomUtil.getLong(elm, "id")));
//				resources.add(MapperHelper.mapUnknown(elm));
			}
			return resources;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
