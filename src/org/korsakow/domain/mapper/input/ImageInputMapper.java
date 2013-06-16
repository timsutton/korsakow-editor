package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Image;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.proxy.ImageProxy;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.services.finder.ImageFinder;

public class ImageInputMapper {
	
	public static Image map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Image.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = null;
			Image a = null;
			rs = ImageFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getImage(rs);
			rs.close();
			return a;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	public static List<IImage> findAll() throws MapperException {
		try {
			List<IImage> list = new ArrayList<IImage>();
			ResultSet rs = null;
			rs = ImageFinder.findAll();
			while (rs.next()) {
				list.add(new ImageProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	private static Image getImage(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Image.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Image image = ImageFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				rs.getString("filename"),
				rs.getObject("duration")!=null?rs.getLong("duration"):null
				);		
		return image;
	}
}
