package org.korsakow.domain.mapper.input;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.korsakow.domain.Media;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.finder.ResourceFinder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class MediaInputMapper {
	public static <M extends IMedia> M map(long id, Class<M> clazz) throws MapperException{
		IResource resource = ResourceInputMapper.map(id);
		if (!clazz.isAssignableFrom(resource.getClass()))
			throw new MapperException("expected: " + clazz.getCanonicalName() + " but found: " + resource.getClass().getCanonicalName());
		return clazz.cast(resource);
	}
	public static IMedia map(long id) throws MapperException{
		IResource resource = ResourceInputMapper.map(id);
		return (IMedia)resource;
	}
	public static Collection<IMedia> findSnuableNotUsedAsSnuMainMedia() throws MapperException {
		List<IMedia> list = new ArrayList<IMedia>();
		try {
			NodeList nodeList = ResourceFinder.findSnuableMediaNotUsedAsSnuMainMedia();
			int length = nodeList.getLength();
			for (int i = 0; i < length; ++i)
				list.add((Media)MapperHelper.mapUnknown((Element)nodeList.item(i)));
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<IMedia> findAll() throws MapperException {
		List<IMedia> list = new ArrayList<IMedia>();
		try {
			NodeList nodeList = ResourceFinder.findAllMedia();
			int length = nodeList.getLength();
			for (int i = 0; i < length; ++i)
				list.add((Media)MapperHelper.mapUnknown((Element)nodeList.item(i)));
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static Collection<IMedia> findReferencedMedia() throws MapperException, XPathExpressionException, SQLException
	{
		Set<IMedia> referenced = new HashSet<IMedia>();
		
//		// TODO: obviously maintaining this list is not the way to go
//		// I would like to have it so the structure clearly differentiates between an ID declaration and
//		// a reference to an object's id
//		// something along the lines of <Snu><mainMedia><refId>1</refId></mainMedia></Snu>
		for (String ELEMENT : MapperHelper.REFERENCING_ELEMENTS)
		{
			NodeList elements = DataRegistry.getDocument().getElementsByTagName(ELEMENT);
			int length = elements.getLength();
			for (int i = 0; i < length; ++i) {
				Element element = (Element)elements.item(i);
				long refId = Long.parseLong(element.getTextContent());
				try {
					IResource resource = ResourceInputMapper.map(refId);
					if (resource instanceof IMedia)
						referenced.add((IMedia)resource);
				} catch (DomainObjectNotFoundException e) {
					e = null;
				}
			}
		}
		return referenced;
	}
}
